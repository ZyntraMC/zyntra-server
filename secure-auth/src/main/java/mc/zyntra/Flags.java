package mc.zyntra;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Flags {

    CAPTCHA("§cRealize a verificação do captcha para continuar o processo de autenticação.",
            "§cPerform captcha verification to continue the authentication process."),

    REGISTER("§cUtilize '§e/cadastrar [senha] [confirmar senha]'§c para cadastrar-se.",
            "§cUse '§e/register [password] [confirm password]'§c to register."),
    REGISTERED("§aVocê completou o estágio de cadastro com sucesso!",
            "§aYou have successfully completed the registration stage!"),

    LOGIN("§cUtilize '§e/logar [senha]'§c para entrar.",
            "§cUse '§e/login [password]' §cto log in."),
    LOGGED("§aVocê entrou com sucesso!",
            "§aYou have successfully logged in!"),

    TITLE_LOGIN("§f/logar [senha]", "§f/login [password]"),
    TITLE_REGISTER("§f/cadastrar [senha] [c. senha]", "§f/register [password] [confirm password]");

    private String pt_br, en_us;
}
