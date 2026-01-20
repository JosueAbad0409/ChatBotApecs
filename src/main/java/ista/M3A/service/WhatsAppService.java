package ista.M3A.service;

import ista.M3A.dto.WhatsAppPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WhatsAppService {

    @Autowired private RestTemplate restTemplate;

    // --- LECTURA DE VARIABLES DE ENTORNO (NUBE) ---
    @Value("${whatsapp.api.url}") 
    private String apiUrl;

    @Value("${whatsapp.api.token}") 
    private String apiToken;

    @Value("${whatsapp.api.phoneNumberId}") 
    private String phoneId;
    
    @Value("${app.advisor.phone}") 
    private String advisorPhone;

    // --- RECEPCIÓN ---
    public void processMessage(WhatsAppPayload payload) {
        try {
            if (payload.getEntry() == null || payload.getEntry().isEmpty()) return;
            var change = payload.getEntry().get(0).getChanges().get(0);
            if (change.getValue().getMessages() == null || change.getValue().getMessages().isEmpty()) return;

            var message = change.getValue().getMessages().get(0);
            String senderPhone = message.getFrom();
            String text = "";

            if ("text".equals(message.getType())) {
                text = message.getText().getBody();
            } else if ("interactive".equals(message.getType())) {
                text = message.getInteractive().getButton_reply().getId();
            }

            System.out.println("📩 MENSAJE DE: " + senderPhone);
            processConversationLogic(senderPhone, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processManualTest(String phone, String message) {
        processConversationLogic(phone, message);
    }

    // --- LÓGICA DEL BOT ---
    private void processConversationLogic(String senderPhone, String textInput) {
        String text = textInput.trim().toLowerCase();

        if (text.contains("hola") || text.contains("buenas") || text.contains("inicio")) {
            sendMenuButtons(senderPhone, "¡Hola! Bienvenido a APECS 🚀\nSelecciona una opción:");
        } 
        else if (text.equals("btn_cursos")) {
            sendText(senderPhone, "🎓 *Nuestros Cursos:*\n1. Java Spring Boot\n2. Análisis de Datos\n3. Ofimática IA");
        } 
        else if (text.equals("btn_academia")) {
            sendText(senderPhone, "🏗️ *Crear Academia*\nDéjanos tu Nombre y RUC para contactarte.");
        }
        else {
            sendText(senderPhone, "🤖 Soy el Bot APECS. Escribe 'Hola' para ver el menú.");
        }
    }

    // --- ENVÍO ---
    private void sendText(String to, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "text");
        body.put("text", Map.of("body", message));
        executeSend(body);
    }

    private void sendMenuButtons(String to, String header) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "interactive");
        
        Map<String, Object> interactive = new HashMap<>();
        interactive.put("type", "button");
        interactive.put("body", Map.of("text", header));
        
        Map<String, Object> action = new HashMap<>();
        action.put("buttons", List.of(
            Map.of("type", "reply", "reply", Map.of("id", "btn_cursos", "title", "Ver Cursos 📚")),
            Map.of("type", "reply", "reply", Map.of("id", "btn_academia", "title", "Crear Academia 🎓"))
        ));
        
        interactive.put("action", action);
        body.put("interactive", interactive);
        executeSend(body);
    }

    private void executeSend(Map<String, Object> body) {
        try {
            String url = apiUrl + "/" + phoneId + "/messages";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Enviado a Meta");
        } catch (Exception e) {
            System.err.println("❌ Error enviando: " + e.getMessage());
        }
    }
}