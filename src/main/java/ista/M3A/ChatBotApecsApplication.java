package ista.M3A;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatBotApecsApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ChatBotApecsApplication.class);
        
        // --- FUERZA BRUTA: OBLIGAR A USAR EL PUERTO 8090 ---
        app.setDefaultProperties(Collections.singletonMap("server.port", "8090"));
        
        app.run(args);
        
        System.out.println("\n✅ SERVIDOR INICIADO EN PUERTO 8090.");
        System.out.println("🚀 ¡EL BOT ESTÁ LISTO! MANDA EL 'HOLA' DESDE EL CELULAR.\n");
    }

    // 🗑️ AQUÍ BORRAMOS EL @BEAN PORQUE YA LO TIENES EN "RestConfig.java"
}