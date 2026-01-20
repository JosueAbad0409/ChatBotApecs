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

    // --- CONFIGURACIÓN (Con valores por defecto para que no falle) ---
    private String apiUrl = "https://graph.facebook.com/v22.0";

    // 2. TU TOKEN REAL (Pégalo dentro de las comillas)
    private String apiToken = "EAARN5YPNF8ABQs7LAhSqR5sKkHbJHVrXE5nfauhdUMMUreFAe08sFZBYZAsDePa85jNQGRVc7vfZA0pIPaaqxiDvc4DFK4ptPGexjKttOvU1WEKebgJrZAeEaeVooyfPgZBKEbeBYikZAC49ib5iIUYpDZBWc3i9mSgZAYjpda4EXpKVYKW8wnZBmu3OJCS7jEcrpSj16Ruf2gJwBl6bjgvzInbuzU4ppLRhbZAvU4efcPzPRspn3xdpAhzPCK7zynlcf1zX5avAfIMxhVTzcA9NcwMShnApN6BtTnUYjS";

    // 3. ID DEL TELÉFONO (El del Bot)
    private String phoneId = "928456293689151";

    // 4. MODO SIMULACIÓN: FALSE (Para que envíe de verdad)
    private boolean simulationMode = false;
    
    // 5. TU TELÉFONO (Para los links)
    private String advisorPhone = "593979744431";
    
    // --- 1. RECEPCIÓN DE MENSAJES (Webhook Meta) ---
 // --- 1. RECEPCIÓN DE MENSAJES (Webhook Meta) ---
    public void processMessage(WhatsAppPayload payload) {
        System.out.println("📨 1. JSON Recibido en Service. Procesando..."); // CHIVATO 1

        try {
            // Validación de nulidad
            if (payload.getEntry() == null || payload.getEntry().isEmpty()) {
                System.out.println("❌ Error: El Payload no tiene 'entry'");
                return;
            }
            
            var change = payload.getEntry().get(0).getChanges().get(0);
            if (change.getValue().getMessages() == null || change.getValue().getMessages().isEmpty()) {
                System.out.println("⚠️ Notificación recibida, pero NO es un mensaje de chat (puede ser estado 'leído'). Ignorando.");
                return;
            }

            // Extracción de datos
            var message = change.getValue().getMessages().get(0);
            String senderPhone = message.getFrom(); // Quién envía
            String text = "";

            if ("text".equals(message.getType())) {
                text = message.getText().getBody();
            } else if ("interactive".equals(message.getType())) {
                text = message.getInteractive().getButton_reply().getId();
            }
            
            System.out.println("✅ 2. Mensaje extraído correctamente:"); // CHIVATO 2
            System.out.println("   👤 De: " + senderPhone);
            System.out.println("   💬 Dice: " + text);

            // Llamada a la lógica
            processConversationLogic(senderPhone, text);

        } catch (Exception e) {
            System.err.println("❌ CRASH en processMessage: ");
            e.printStackTrace(); // Esto nos dirá el error exacto si falla el código
        }
    }
    
    // --- 2. RECEPCIÓN MANUAL (Para pruebas Swagger) ---
    public void processManualTest(String phone, String message) {
        System.out.println("🔧 TEST MANUAL RECIBIDO: " + message);
        processConversationLogic(phone, message);
    }

    // --- 3. CEREBRO APECS (Lógica del Negocio) ---
    private void processConversationLogic(String senderPhone, String textInput) {
        String text = textInput.trim().toLowerCase();
        System.out.println("🧠 APECS ANALIZANDO: " + text);

        // --- NIVEL 1: SALUDO ---
        if (esSaludo(text)) {
            String mensajeBienvenida = "¡Hola! Bienvenido a APECS. Somos expertos en Educación y Capacitación Tecnológica.\n" +
                                       "Para brindarte la mejor información, por favor selecciona una opción:";
            sendMenuButtons(senderPhone, mensajeBienvenida);
        } 
        
        // --- NIVEL 2: MOSTRAR LISTA DE CURSOS ---
        else if (text.equals("btn_cursos") || text.contains("curso")) {
            String respuestaCursos = "¿Qué habilidad quieres dominar hoy?\n\n" +
                                     "1. Ofimática con IA 📊\n" +
                                     "2. Análisis de Datos 📈\n" +
                                     "3. Programación 💻\n" +
                                     "4. Habilidades Blandas 🗣️\n" +
                                     "5. Ver Todo 📂\n\n" +
                                     "👉 *Escribe el número de la opción que te interese (ej: 3)*";
            sendText(senderPhone, respuestaCursos);
        }

        // --- NIVEL 3: SELECCIÓN DE CURSO (Estrategia Link Directo GRATIS) ---
        
        else if (text.equals("1") || text.contains("ofimática")) {
            String link = generarLinkWhatsApp("Hola, quiero información del curso de Ofimática con IA");
            sendText(senderPhone, "📊 *Ofimática con IA*\nDomina Excel y herramientas inteligentes.\n\n👇 *Toca aquí para hablar con el Asesor y ver temario:*\n" + link);
        }
        
        else if (text.equals("2") || text.contains("datos")) {
            String link = generarLinkWhatsApp("Hola, quiero información del curso de Análisis de Datos");
            sendText(senderPhone, "📈 *Análisis de Datos*\nAprende a tomar decisiones con datos reales.\n\n👇 *Solicitar detalles al Asesor:*\n" + link);
        }

        else if (text.equals("3") || text.contains("programación")) {
            String link = generarLinkWhatsApp("Hola, quiero información del curso de Programación");
            sendText(senderPhone, "💻 *Programación y Soporte*\nDesarrollo Java, Spring Boot y Android.\n\n👇 *Hablar con el Ing. Josué (Experto):*\n" + link);
        }

        else if (text.equals("4") || text.contains("blandas")) {
            String link = generarLinkWhatsApp("Hola, quiero información sobre Habilidades Blandas");
            sendText(senderPhone, "🗣️ *Habilidades Blandas*\nLiderazgo y comunicación efectiva.\n\n👇 *Contactar Asesor:*\n" + link);
        }
        
        else if (text.equals("5") || text.contains("todo")) {
            String link = generarLinkWhatsApp("Hola, envíame el Catálogo Completo PDF");
            sendText(senderPhone, "📂 *Catálogo Completo*\n\n👇 *Pídelo directamente aquí:*\n" + link);
        }

        // --- NIVEL 2 B: ACADEMIA VIRTUAL ---
        else if (text.equals("btn_academia") || text.contains("academia")) {
            String respuestaAcademia = "¡Entendido! Creamos Tu Propia Plataforma de Capacitación.\n" +
                                       "Por favor, déjanos tus datos para que un Asesor de Proyectos te contacte:\n\n" +
                                       "1. Tu Nombre.\n" +
                                       "2. Cédula o RUC.";
            sendText(senderPhone, respuestaAcademia);
        }
        
        // --- CAPTURA DE DATOS (Nombre/RUC) ---
        else if (text.length() > 5 && (text.matches(".*\\d.*") || text.split(" ").length > 1)) {
            // Aquí generamos un link para que el cliente confirme sus datos contigo
            String linkConfirmacion = generarLinkWhatsApp("Hola, mis datos para la Academia son: " + textInput);
            
            sendText(senderPhone, "✅ Datos recibidos.\n\n👇 *Haz clic aquí para finalizar tu registro con el Asesor:*\n" + linkConfirmacion);
        }
        
        // --- FALLBACK (No entendió) ---
        else {
            sendMenuButtons(senderPhone, "🤔 No entendí esa opción. Por favor selecciona del menú:"); 
        }
    }

    // --- MÉTODOS AUXILIARES ---
    
    private boolean esSaludo(String text) {
        return text.contains("hola") || text.contains("inicio") || text.contains("buenas") || text.contains("info");
    }
    
    private String generarLinkWhatsApp(String mensaje) {
        // Reemplaza espacios por %20 para que el link funcione
        String mensajeCodificado = mensaje.replace(" ", "%20");
        return "https://wa.me/" + advisorPhone + "?text=" + mensajeCodificado;
    }

    // --- ENVÍO DE MENSAJES ---

    public void sendText(String to, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "text");
        body.put("text", Map.of("body", message));
        executeSend(body);
    }

    public void sendMenuButtons(String to, String header) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "interactive");
        
        Map<String, Object> interactive = new HashMap<>();
        interactive.put("type", "button");
        interactive.put("body", Map.of("text", header));
        
        Map<String, Object> action = new HashMap<>();
        action.put("buttons", List.of(
            createButton("btn_cursos", "Ver Cursos 📚"),
            createButton("btn_academia", "Crear Academia 🎓")
        ));
        
        interactive.put("action", action);
        body.put("interactive", interactive);
        executeSend(body);
    }
    
    private Map<String, Object> createButton(String id, String title) {
        return Map.of("type", "reply", "reply", Map.of("id", id, "title", title));
    }

    // --- MOTOR DE ENVÍO (Simulación vs Real) ---
    private void executeSend(Map<String, Object> body) {
        if (simulationMode) {
            System.out.println("\n------------------------------------------------");
            System.out.println("🧪 [SIMULACIÓN] RESPUESTA BOT");
            System.out.println("📱 Para: " + body.get("to"));
            
            if("text".equals(body.get("type"))) {
                Map<String, String> txt = (Map<String, String>) body.get("text");
                System.out.println("💬 MENSAJE:\n" + txt.get("body"));
            } 
            else if ("interactive".equals(body.get("type"))) {
                System.out.println("🔘 MENÚ DE BOTONES:");
                try {
                    Map<String, Object> interactive = (Map<String, Object>) body.get("interactive");
                    Map<String, String> bodyText = (Map<String, String>) interactive.get("body");
                    System.out.println("📝 Header: " + bodyText.get("text"));
                    
                    Map<String, Object> action = (Map<String, Object>) interactive.get("action");
                    List<Map<String, Object>> buttons = (List<Map<String, Object>>) action.get("buttons");
                    for(Map<String, Object> btn : buttons) {
                        Map<String, String> reply = (Map<String, String>) btn.get("reply");
                        System.out.println("   👉 [" + reply.get("id") + "] " + reply.get("title"));
                    }
                } catch (Exception e) {}
            }
            System.out.println("------------------------------------------------\n");
        } else {
             try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(apiToken);
                String url = apiUrl + "/" + phoneId + "/messages";
                restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
            } catch (Exception e) {
                System.err.println("❌ Error enviando a API Meta: " + e.getMessage());
            }
        }
    }
}