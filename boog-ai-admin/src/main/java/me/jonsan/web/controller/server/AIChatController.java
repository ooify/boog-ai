package me.jonsan.web.controller.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import me.jonsan.common.annotation.Anonymous;
import me.jonsan.common.core.domain.AjaxResult;
import me.jonsan.server.domain.ModelInfo;
import me.jonsan.server.domain.Prompt;
import me.jonsan.server.domain.SecretKey;
import me.jonsan.server.service.IModelInfoService;
import me.jonsan.server.service.IPromptService;
import me.jonsan.server.service.ISecretKeyService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/server/ai")
public class AIChatController {
    private static final String OLLAMA_PROVIDER = "ollama";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json");

    @Autowired
    private IModelInfoService modelInfoService;

    @Autowired
    private IPromptService promptService;

    @Autowired
    private ISecretKeyService secretKeyService;

    @Value("${zilliz.url}")
    private String zillizUrl;

    @Value("${zilliz.token}")
    private String zillizToken;

    @Anonymous
    @PostMapping("/search/img")
    public AjaxResult imgSearch(@RequestParam("file") MultipartFile img) throws IOException {
        // Get default image model
        ModelInfo imageModel = getDefaultModel("2");
        if (imageModel == null) {
            return AjaxResult.error("没有可用的图像模型");
        }

        // Get default prompt for image model
        Prompt imagePrompt = getDefaultPrompt(imageModel.getModelId(), "1");
        if (imagePrompt == null) {
            return AjaxResult.error("没有可用的图像提示词");
        }

        // Get secret key
        SecretKey imageModelSecretKey = getSecretKey(imageModel.getModelId());
        if (imageModelSecretKey == null) {
            return AjaxResult.error("没有可用的密钥");
        }

        // Convert image to base64
        String base64Image = Base64.getEncoder().encodeToString(img.getBytes());
        String contentType = img.getContentType();
        String imageUrl;
        
        // Set the correct content type for base64
        if (contentType != null) {
            switch (contentType.toLowerCase()) {
                case "image/png":
                    imageUrl = "data:image/png;base64," + base64Image;
                    break;
                case "image/jpeg":
                case "image/jpg":
                    imageUrl = "data:image/jpeg;base64," + base64Image;
                    break;
                case "image/webp":
                    imageUrl = "data:image/webp;base64," + base64Image;
                    break;
                default:
                    // Default to JPEG if content type is not recognized
                    imageUrl = "data:image/jpeg;base64," + base64Image;
            }
        } else {
            // Default to JPEG if content type is null
            imageUrl = "data:image/jpeg;base64," + base64Image;
        }

        // Prepare request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", imageModel.getModelCode());
        requestBody.put("stream", false);

        JSONArray messages = new JSONArray();
        
        // Add system message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        JSONArray systemContent = new JSONArray();
        JSONObject systemText = new JSONObject();
        systemText.put("type", "text");
        systemText.put("text", imagePrompt.getPromptText());
        systemContent.add(systemText);
        systemMessage.put("content", systemContent);
        messages.add(systemMessage);

        // Add user message with image
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        JSONArray userContent = new JSONArray();
        JSONObject imageContent = new JSONObject();
        imageContent.put("type", "image_url");
        JSONObject imageUrlObj = new JSONObject();
        imageUrlObj.put("url", imageUrl);
        imageContent.put("image_url", imageUrlObj);
        userContent.add(imageContent);
        userMessage.put("content", userContent);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        // Send request to image model
        OkHttpClient client = new OkHttpClient();
        Request request = buildRequest(imageModel.getApiEndpoint(), requestBody, imageModelSecretKey);
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return AjaxResult.error("图像模型请求失败");
        }

        // Parse response to get generated title
        JSONObject responseJson = JSON.parseObject(response.body().string());
        String generatedTitle = responseJson.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

        // Get embedding model
        ModelInfo embeddingModel = getDefaultModel("3");
        if (embeddingModel == null) {
            return AjaxResult.error("没有可用的嵌入模型");
        }

        // Get title embedding
        String titleEmbedding = getTitleEmbedding(client, embeddingModel, getSecretKey(embeddingModel.getModelId()), generatedTitle);
        if (titleEmbedding == null) {
            return AjaxResult.error("获取标题向量失败");
        }

        // Search similar books
        JSONArray dataArray = searchSimilarBooks(titleEmbedding);
        if (dataArray == null) {
            return AjaxResult.error("搜索相似书籍失败");
        }

        // Extract book IDs
        List<Long> bookIds = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            bookIds.add(dataArray.getJSONObject(i).getLong("id"));
        }
        
        return AjaxResult.success(bookIds);
    }

    @Anonymous
    @PostMapping("/generate")
    public void chat(@org.springframework.web.bind.annotation.RequestBody String prompt, HttpServletResponse response) throws Exception {
        // Get default text model
        ModelInfo textModel = getDefaultModel("1");
        if (textModel == null) {
            sendError(response, "没有可用的文本模型");
            return;
        }

        // Get default embedding model
        ModelInfo embeddingModel = getDefaultModel("3");
        if (embeddingModel == null) {
            sendError(response, "没有可用的嵌入模型");
            return;
        }

        // Get prompts
        Prompt generatePrompt = getDefaultPrompt(textModel.getModelId(), "0");
        Prompt chatPrompt = getDefaultPrompt(textModel.getModelId(), "1");
        if (generatePrompt == null || chatPrompt == null) {
            sendError(response, "没有可用的提示词");
            return;
        }

        // Get secret keys
        SecretKey textModelSecretKey = getSecretKey(textModel.getModelId());
        SecretKey embeddingModelSecretKey = getSecretKey(embeddingModel.getModelId());

        OkHttpClient client = new OkHttpClient();

        // Generate book title
        String generatedTitle = generateBookTitle(client, textModel, textModelSecretKey, generatePrompt, prompt);
        if (generatedTitle == null) {
            sendError(response, "生成标题失败");
            return;
        }

        // Get title embedding
        String titleEmbedding = getTitleEmbedding(client, embeddingModel, embeddingModelSecretKey, generatedTitle);
        if (titleEmbedding == null) {
            sendError(response, "获取标题向量失败");
            return;
        }

        // Search similar books
        JSONArray dataArray = searchSimilarBooks(titleEmbedding);

        List<Long> bookIds = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            bookIds.add(dataArray.getJSONObject(i).getLong("id"));
        }
        if (bookIds == null) {
            sendError(response, "搜索相似书籍失败");
            return;
        }

        // Generate chat response
        generateChatResponse(client, textModel, textModelSecretKey, chatPrompt, prompt, dataArray, response);
    }

    private ModelInfo getDefaultModel(String modelType) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setIsDefault("1");
        modelInfo.setModelType(modelType);
        List<ModelInfo> modelInfos = modelInfoService.selectModelInfoList(modelInfo);
        return modelInfos.isEmpty() ? null : modelInfos.get(0);
    }

    private Prompt getDefaultPrompt(Long modelId, String tag) {
        Prompt promptInfo = new Prompt();
        promptInfo.setIsDefault("1");
        promptInfo.setTag(tag);
        promptInfo.setModelId(modelId);
        List<Prompt> prompts = promptService.selectPromptList(promptInfo);
        return prompts.isEmpty() ? null : prompts.get(0);
    }

    private SecretKey getSecretKey(Long modelId) {
        SecretKey secretKey = new SecretKey();
        secretKey.setIsDefault("1");
        secretKey.setStatus("1");
        secretKey.setModelId(modelId);
        List<SecretKey> secretKeys = secretKeyService.selectSecretKeyList(secretKey);
        return secretKeys.isEmpty() ? null : secretKeys.get(0);
    }

    private String generateBookTitle(OkHttpClient client, ModelInfo model, SecretKey secretKey,
                                     Prompt prompt, String userPrompt) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model.getModelCode());
        requestBody.put("stream", false);

        JSONArray messages = new JSONArray();
        messages.add(createMessage("system", prompt.getPromptText()));
        messages.add(createMessage("user", userPrompt));
        requestBody.put("messages", messages);

        Request request = buildRequest(model.getApiEndpoint(), requestBody, secretKey);
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return null;
        }

        JSONObject responseJson = JSON.parseObject(response.body().string());
        if (OLLAMA_PROVIDER.equals(model.getProvider())) {
            return responseJson.getJSONObject("message").getString("content");
        } else {
            return responseJson.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
    }

    private String getTitleEmbedding(OkHttpClient client, ModelInfo model, SecretKey secretKey,
                                     String title) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);

        Request request = buildRequest(model.getApiEndpoint(), requestBody, secretKey);
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return null;
        }

        return JSON.parseObject(response.body().string()).getString("embedding");
    }

    private JSONArray searchSimilarBooks(String titleEmbedding) throws IOException {
        JSONArray embeddingArray = JSONArray.parseArray(titleEmbedding);
        JSONArray outerDataArray = new JSONArray();
        outerDataArray.add(embeddingArray);

        JSONObject requestBody = new JSONObject();
        requestBody.put("collectionName", "book");
        requestBody.put("data", outerDataArray);
        requestBody.put("limit", 3);

        JSONArray outputFields = new JSONArray();
        outputFields.add("id");
        outputFields.add("title");
        outputFields.add("description");
        requestBody.put("outputFields", outputFields);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(zillizUrl)
                .post(okhttp3.RequestBody.create(requestBody.toString(), JSON_MEDIA_TYPE))
                .header("Authorization", "Bearer " + zillizToken)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return null;
        }

        JSONObject responseJson = JSON.parseObject(response.body().string());
        JSONArray dataArray = responseJson.getJSONArray("data");
//        List<Long> bookIds = new ArrayList<>();
//        for (int i = 0; i < dataArray.size(); i++) {
//            bookIds.add(dataArray.getJSONObject(i).getLong("id"));
//        }
        return dataArray;
    }

    private void generateChatResponse(OkHttpClient client, ModelInfo model, SecretKey secretKey,
//                                    Prompt prompt, String userPrompt, List<Long> bookIds,
                                      Prompt prompt, String userPrompt, JSONArray jsonData,
                                      HttpServletResponse response) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model.getModelCode());
        requestBody.put("stream", true);

        JSONArray messages = new JSONArray();
        messages.add(createMessage("system", prompt.getPromptText() + "JSON数据：" + jsonData.toString()));
        messages.add(createMessage("user", userPrompt));
        requestBody.put("messages", messages);

        Request request = buildRequest(model.getApiEndpoint(), requestBody, secretKey);

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();

        List<Long> bookIds = new ArrayList<>();
        for (int i = 0; i < jsonData.size(); i++) {
            bookIds.add(jsonData.getJSONObject(i).getLong("id"));
        }

        // Send book IDs first
        JSONObject bookIdsMessage = new JSONObject();
        bookIdsMessage.put("type", "book_ids");
        bookIdsMessage.put("book_ids", bookIds);
        out.write((bookIdsMessage.toJSONString() + "\n").getBytes(StandardCharsets.UTF_8));
        out.flush();

        Response ollamaResponse = client.newCall(request).execute();
        if (ollamaResponse.body() != null) {
            handleStreamResponse(ollamaResponse, out, bookIds);
        }
    }

    private void handleStreamResponse(Response response, ServletOutputStream out, List<Long> bookIds) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder lastLine = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("data: ")) {
                line = line.substring(6); // Remove "data: " prefix
            }

            if (line.trim().isEmpty()) {
                continue;
            }

            // Handle [DONE] marker
            if (line.trim().equals("[DONE]")) {
                // Create final message in Ollama format
                JSONObject finalMessage = new JSONObject();
                finalMessage.put("model", "model"); // This will be replaced by the actual model name
                finalMessage.put("created_at", System.currentTimeMillis());

                JSONObject message = new JSONObject();
                message.put("role", "assistant");
                message.put("content", "");
                finalMessage.put("message", message);

                finalMessage.put("done", true);
                finalMessage.put("done_reason", "stop");

                lastLine.setLength(0);
                lastLine.append(finalMessage.toJSONString());
                continue;
            }

            try {
                JSONObject responseJson = JSON.parseObject(line);
                JSONObject ollamaFormat = new JSONObject();

                // Set common fields
                ollamaFormat.put("model", responseJson.getString("model"));
                ollamaFormat.put("created_at", responseJson.getString("created"));

                // Handle message content
                JSONObject message = new JSONObject();
                message.put("role", "assistant");

                if (responseJson.containsKey("choices")) {
                    // Other provider format
                    JSONObject choice = responseJson.getJSONArray("choices").getJSONObject(0);
                    if (choice.containsKey("delta")) {
                        JSONObject delta = choice.getJSONObject("delta");
                        message.put("content", delta.getString("content"));
                    } else if (choice.containsKey("message")) {
                        message.put("content", choice.getJSONObject("message").getString("content"));
                    }

                    // Check if this is the last message
                    boolean isDone = choice.getString("finish_reason") != null;
                    ollamaFormat.put("done", isDone);
                    if (isDone) {
                        ollamaFormat.put("done_reason", choice.getString("finish_reason"));
                    }
                } else {
                    // Ollama format
                    message.put("content", responseJson.getJSONObject("message").getString("content"));
                    ollamaFormat.put("done", responseJson.getBoolean("done"));
                    if (responseJson.containsKey("done_reason")) {
                        ollamaFormat.put("done_reason", responseJson.getString("done_reason"));
                    }
                }

                ollamaFormat.put("message", message);

                // Add additional Ollama fields if they exist
                if (responseJson.containsKey("total_duration")) {
                    ollamaFormat.put("total_duration", responseJson.getLong("total_duration"));
                }
                if (responseJson.containsKey("load_duration")) {
                    ollamaFormat.put("load_duration", responseJson.getLong("load_duration"));
                }
                if (responseJson.containsKey("prompt_eval_count")) {
                    ollamaFormat.put("prompt_eval_count", responseJson.getLong("prompt_eval_count"));
                }
                if (responseJson.containsKey("prompt_eval_duration")) {
                    ollamaFormat.put("prompt_eval_duration", responseJson.getLong("prompt_eval_duration"));
                }
                if (responseJson.containsKey("eval_count")) {
                    ollamaFormat.put("eval_count", responseJson.getLong("eval_count"));
                }
                if (responseJson.containsKey("eval_duration")) {
                    ollamaFormat.put("eval_duration", responseJson.getLong("eval_duration"));
                }

                // Save last line if it's the final message
                if (ollamaFormat.getBoolean("done")) {
                    lastLine.setLength(0);
                    lastLine.append(ollamaFormat.toJSONString());
                } else {
                    out.write((ollamaFormat.toJSONString() + "\n").getBytes(StandardCharsets.UTF_8));
                    out.flush();
                }
            } catch (Exception e) {
                // Log error and continue processing
                System.err.println("Error processing line: " + line);
                e.printStackTrace();
            }
        }

        // Handle the last message
        if (!lastLine.isEmpty()) {
            JSONObject lastJson = JSON.parseObject(lastLine.toString());
            lastJson.put("book_ids", bookIds);
            out.write((lastJson.toJSONString() + "\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }

    private JSONObject createMessage(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private Request buildRequest(String url, JSONObject requestBody, SecretKey secretKey) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(requestBody.toString(), JSON_MEDIA_TYPE));

        if (secretKey != null) {
            builder.header("Authorization", "Bearer " + secretKey.getKeyValue());
        }

        return builder.build();
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(AjaxResult.error(message)));
    }
}
