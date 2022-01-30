import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MojangAuthenticationService;
import com.github.steveice10.mc.auth.service.MsaAuthenticationService;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Auth extends net.patyhank.mcbot.auth.Auth {
    private String username,password;
    private boolean usingMS;

    public Auth() {
        init();
    }

    public void init( ) {
        try {
            File file = new File("./auth.yml");
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    return;
                }
                AuthData authData = new AuthData();
                authData.setEmail("your@email.com");
                authData.setPassword("123456789");
                authData.setMs(true);
                new Yaml().dump(authData, new FileWriter(file));
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            CustomClassLoaderConstructor constructor = new CustomClassLoaderConstructor(this.getClass().getClassLoader());
            Auth.AuthData authData = new Yaml(constructor).loadAs(fileInputStream, Auth.AuthData.class);
            this.username = authData.getEmail();
            this.password = authData.getPassword();
            this.usingMS = authData.isMs();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public AuthenticationService auth() {
        if (usingMS) {
            MsaAuthenticationService msaAuthenticationService;
            try {
                msaAuthenticationService = new MsaAuthenticationService("1b3f2c18-6aee-48bc-8aa8-636537d84925");
                msaAuthenticationService.setUsername(username);
                msaAuthenticationService.setPassword(password);
                msaAuthenticationService.login();
                return msaAuthenticationService;
            } catch (IOException | RequestException e) {
                e.printStackTrace();
            }
        } else {
            MojangAuthenticationService mojangAuthenticationService;
            try {
                mojangAuthenticationService = new MojangAuthenticationService();
                mojangAuthenticationService.setUsername(username);
                mojangAuthenticationService.setUsername(password);
                mojangAuthenticationService.login();
                return mojangAuthenticationService;
            } catch (RequestException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Data
    public static class AuthData {
        private boolean ms;
        private String email;
        private String password;
    }
}

