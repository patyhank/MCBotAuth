import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MsaAuthenticationService;
import com.github.steveice10.mc.auth.util.MSALApplicationOptions;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Auth extends net.patyhank.mcbot.auth.Auth {
    public Auth() {

    }

    @Override
    public AuthenticationService auth() {
        MsaAuthenticationService msaAuthenticationService;
        try {
            msaAuthenticationService = new MsaAuthenticationService("1b3f2c18-6aee-48bc-8aa8-636537d84925");
            msaAuthenticationService.setUsername("mail@patyhank.net");
            msaAuthenticationService.setDeviceCodeConsumer(deviceCode -> {
                System.out.println("在 " + deviceCode.verificationUri() + " 輸入:");
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(deviceCode.verificationUri()));
                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(deviceCode.userCode()), null);
                System.out.println(deviceCode.userCode());
            });
            msaAuthenticationService.login();
            return msaAuthenticationService;
        } catch (IOException | RequestException e) {
            e.printStackTrace();
        }
//        return super.auth();
        return null;
    }
}
