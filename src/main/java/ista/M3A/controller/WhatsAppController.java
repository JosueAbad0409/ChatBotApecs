package ista.M3A.controller;

import ista.M3A.dto.ManualRequest;
import ista.M3A.dto.WhatsAppPayload;
import ista.M3A.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired private WhatsAppService service;
    @Value("${whatsapp.webhook.token:apecs_secreto}") 
    private String verifyToken;

    // 1. Verificar Webhook (GET)
    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // 2. Recibir Mensaje (POST)
 // 2. Recibir Mensaje (MODO DEBUG SUPREMO)
    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveMessage(@RequestBody String payloadRaw) {
        
        // 🚨 CHIVATO 1: Confirmar que llegó
        System.out.println("\n------------------------------------------------");
        System.out.println("🔔 ¡TOC TOC! ALGUIEN LLAMÓ A LA PUERTA (Controller)");
        System.out.println("📦 JSON CRUDO RECIBIDO:");
        System.out.println(payloadRaw); // Aquí imprimimos lo que sea que llegue
        System.out.println("------------------------------------------------\n");

        // AQUÍ INTENTAMOS PROCESARLO MANUALMENTE PARA VER SI FALLA EL DTO
        try {
            // Necesitas importar: com.fasterxml.jackson.databind.ObjectMapper
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // Intentamos convertir el texto a tu objeto Java
            WhatsAppPayload payloadObj = mapper.readValue(payloadRaw, WhatsAppPayload.class);
            
            System.out.println("✅ Conversión a Objeto EXITOSA. Pasando al servicio...");
            service.processMessage(payloadObj);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR GRAVE: El JSON llegó, pero tus clases DTO están mal.");
            System.err.println("El error dice: " + e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    // 3. TU PRUEBA MANUAL (POST)
    // Llama a esto para fingir que el bot inicia la charla
    @PostMapping("/trigger")
    public ResponseEntity<String> triggerMessage(@RequestBody ManualRequest request) {
        if (request.getPhone() == null) {
            return ResponseEntity.badRequest().body("Falta el teléfono");
        }

        // Si el mensaje está vacío, asumimos que es un "Hola" inicial
        String mensajeUsuario = (request.getMessage() == null || request.getMessage().isEmpty()) 
                                ? "Hola" 
                                : request.getMessage();

        // LLAMAMOS AL MËTODO DE PRUEBA (SIMULAMOS QUE EL USUARIO ESCRIBIÓ)
        service.processManualTest(request.getPhone(), mensajeUsuario);

        return ResponseEntity.ok("✅ Simulación ejecutada. Revisa la consola de Eclipse.");
    }
    @GetMapping("/test")
    public String probarNavegador() {
        return "<h1>🚀 ¡El Bot APECS está vivo, Papu!</h1> <p>El servidor escucha en el puerto 8090.</p>";
    }
    
}