package mc.zyntra.general;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Constant {

    public static final Pattern NICKNAME_PATTERN = Pattern.compile("\\w{1,16}");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    public static final String SERVER_NAME = "Zyntra";
    public static final String SERVER_ADDRESS = "zyntramc.net";
    public static final String FIRST_COLOR = "§5";
    public static final String SECOND_COLOR = "§d";
    public static final String FORMATTED_NAME = FIRST_COLOR + "§l" + SERVER_NAME.toUpperCase();

    public static final String STORE = SERVER_ADDRESS +"/store";
    public static final String DISCORD = SERVER_ADDRESS +"/discord";
    public static final String FORUM = SERVER_ADDRESS +"/forum";
    public static final String TWITTER = "@ZyntraMC";

    public static final String MONGO_DATABASE_MAIN = SERVER_NAME.toLowerCase() + "data";
    public static final String MONGO_DATABASE_STATISTICS = SERVER_NAME.toLowerCase() + "status";
    public static final String MONGO_COLLECTION_ACCOUNTS = "accounts";
    public static final String MONGO_COLLECTION_CLANS = "clans";

    public static final String TABLIST_HEADER =
            "\n " + FIRST_COLOR + "§l" + SERVER_NAME.toUpperCase() + " \n";

    public static final String TABLIST_FOOTER =
            "\n\n" +
                    " §aVIPs, Tags, Cash §eem: §b" + STORE + " " +
            "\n\n";

    public static final String ACCOUNT_LOAD_FAILED = "§cNão foi possível carregar as informações da sua conta.";
    public static final String NAME_TOO_LONG = "§cO playerName que o mesmo utiliza ultrapassou o limite de caracteres.";

    public static final String YOU_ARE_BANNED = "§e§lPUNIÇÃO ATIVA!\n\n§cSua conta está suspensa %s.\n\n" +
            "§cMotivo: %s\n" +
            "§cExpira em: %s\n" +
            "§cID da punição: #%s\n\n" +
            "§cBanido incorretamente? Faça sua appeal em §n" + Constant.DISCORD;

    public static final String YOU_ARE_BLACKLISTED = "§e§lPUNIÇÃO ATIVA!\n\n§cSua conta está na lista negra.\n\n" +
            "§cMotivo: %s\n" +
            "§cID da punição: #%s\n\n" +
            "Banido incorretamente? Faça sua appeal em §n" + Constant.DISCORD;

    public static final String COMMAND_USAGE = "§cA utilização da variável está invalida, o correto é: §e'/%s'";
    public static final String COMMAND_NO_ACCESS = "§cVocê não possuí permissão para utilizar este comando.";
    public static final String PLAYER_NOT_FOUND = "§cEste jogador não foi encontrado.";

    public static final String GAME_ROOM_AVAILABLE = "\n§a§lNOVA SALA DISPONIVEL!\n§fA sala §b%s §festá disponível.\n§e§lClique aqui para jogar.\n";
    public static final String GAME_ROOM_STARTING = "\n§a§lNOVA SALA DISPONIVEL!\n§fA sala §b%s §festá iniciando.\n§e§lClique aqui para jogar com +§6%s §e§ljogadores.";

    public static final String NO_SERVER_FOUND = "§cNenhum servidor está disponível no momento.";
    public static final String NO_LOBBY_FOUND = "§cNenhum lobby está disponível no momento.";
    public static final String SERVER_NOT_FOUND = "§cNenhum servidor de %s está disponível no momento.";

    public static final String CLAN_SUCCESS_CREATE = "\n§eO clan §6'%s' §7[%s] §efoi criado com sucesso!\n§eVeja as informações do mesmo em §b'/clan ver'\n";

    public static final String[] SWEAR_WORDS = {"mush", "lothus", "redestory", "redesky", "rededark", "mineman", "landsmc", "limbomc", "redeferty", "skycraft",
            "mcmush", "mclothus", "mcredestory", "mcredesky", "mcrededark", "mcmineman", "mclands", "mclimbo", "mcredeferty", "mcskycraft",
            "mushmc", "lothusmc", "redestorymc", "redeskymc", "rededarkmc", "minemanmc", "landsmcmc", "limbomcmc", "redefertymc", "skycraftmc",
            "mushrede", "lothusrede", "redestoryrede", "redeskyrede", "rededarkrede", "minemanrede", "landsrede", "limborede", "redefertyrede", "skyrede",
            "mush", "flame", "brave", "yolo", "hylex", "macaco", "academy", "gorila", "fudido", "puta", "vagabunda",
            "weaven", "nowly", "smash", "pobre", "bitch", "fuck", "nigger", "nigga", "pirata", "lixo", "fudida", "goril", "macaquinho", "macac", "negro", "primata",
            ".com.br", ".com", ".pt", ".eu", ".br", ".net", ".me", "idiota", "estúpido", "imbecil", "otário", "cretino", "cretina", "babaca", "babaca", "merda", "inútil", "panaca", "trouxa", "cretinice", "imbecilidade", "bosta",
            "idiot", "stupid", "moron", "fool", "cretin", "bitch", "shit", "useless", "jerk", "cretinism", "stupidity", "crap",
            "bastard", "asshole", "dumbass", "douchebag", "twat", "wanker", "cunt", "ass", "damn", "hell", "bullshit", "fuckwit", "arsehole", "screw you", "bugger", "piss off",
            "filho da puta", "viado", "cuzão", "vai se foder", "caralho", "merda", "bosta", "babaca", "imbecil", "otário", "idiota", "trouxa", "cretino", "estúpido", "porra", "cacete"};
}
