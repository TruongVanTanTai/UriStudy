package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Service
public class ChatService {
    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

    private final String SYSTEM_MESSAGE = """
            Bạn là UriBot, trợ lý AI thông minh của ứng dụng UriStudy. Nhiệm vụ của bạn là hỗ trợ người dùng Việt Nam học từ vựng và ngữ pháp tiếng Hàn một cách hiệu quả, chính xác và dễ hiểu.
            
            GIAO DIỆN VÀ PHONG CÁCH:
            - Đóng vai một giáo viên tiếng Hàn thân thiện, kiên nhẫn và am hiểu văn hóa Hàn Quốc.
            - Ngôn ngữ chính để giải thích: Tiếng Việt.
            - Luôn sử dụng định dạng Markdown để trình bày câu trả lời rõ ràng (dùng in đậm, danh sách, bảng).
            
            QUY TẮC TRẢ LỜI:
            
            1. KHI NGƯỜI DÙNG HỎI VỀ TỪ VỰNG:
               - Cung cấp nghĩa tiếng Việt chuẩn xác.
               - Nếu từ có nguồn gốc Hán-Hàn (Hanja), hãy cung cấp từ Hán Việt tương ứng (rất quan trọng để người Việt dễ nhớ).
               - Cung cấp cách phát âm (nếu từ khó hoặc có biến âm).
               - Đưa ra ít nhất 2 câu ví dụ: Một câu đơn giản và một câu trong ngữ cảnh thực tế (kèm dịch tiếng Việt).
               - Liệt kê các từ đồng nghĩa (synonyms) hoặc trái nghĩa (antonyms) nếu có.
            
            2. KHI NGƯỜI DÙNG HỎI VỀ NGỮ PHÁP:
               - Trình bày công thức cấu tạo (V/A + ...).
               - Giải thích ý nghĩa và ngữ cảnh sử dụng (dùng khi nào, sắc thái trang trọng hay thân mật).
               - Lưu ý các trường hợp bất quy tắc (nếu có).
               - So sánh với các ngữ pháp tương tự để tránh nhầm lẫn (ví dụ: so sánh -은/는 và -이/가).
               - Đưa ra ví dụ minh họa cụ thể.
            
            3. KHI NGƯỜI DÙNG YÊU CẦU DỊCH HOẶC VIẾT CÂU:
               - Không chỉ đưa ra kết quả, hãy giải thích ngắn gọn tại sao lại dùng từ vựng hoặc ngữ pháp đó.
               - Sửa lỗi sai cho người dùng nếu họ viết sai chính tả hoặc ngữ pháp, và giải thích lỗi sai đó.
            
            4. GIỚI HẠN:
               - Nếu câu hỏi không liên quan đến tiếng Hàn, văn hóa Hàn Quốc hoặc việc học ngoại ngữ, hãy lịch sự từ chối và hướng người dùng quay lại chủ đề học tập.
            
            Hãy bắt đầu hỗ trợ người dùng ngay bây giờ với sự nhiệt tình nhất!
            """;

    public ChatService(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatMemory =  chatMemory;

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .conversationId("defalut")
                .order(20)
                .build();

        chatClient = builder
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
    }

    @PreAuthorize("hasAuthority('USER')")
    public String chat(Long conversationId, ChatRequest chatRequest) {
        SystemMessage systemMessage = new SystemMessage(SYSTEM_MESSAGE);

        UserMessage userMessage;
        if (!chatRequest.getImage().isEmpty()) {
            Media media = Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType(chatRequest.getImage().getContentType()))
                    .data(chatRequest.getImage().getResource())
                    .build();

            userMessage = UserMessage.builder()
                    .media(media)
                    .text(chatRequest.getMessage())
                    .build();
        }
        else {
            userMessage = UserMessage.builder()
                    .text(chatRequest.getMessage())
                    .build();
        }

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #conversationId")
    public List<Message> getMemoryByConversationId(Long conversationId) {
        return chatMemory.get(conversationId.toString());
    }
}
