package mc.zyntra.general;

import mc.zyntra.backend.nosql.MongoConnection;
import mc.zyntra.backend.packet.RedisConnection;
import mc.zyntra.general.account.friend.FriendController;
import mc.zyntra.general.controller.AccountController;
import mc.zyntra.general.controller.ReportController;
import mc.zyntra.general.controller.StatisticsController;
import mc.zyntra.general.controller.WhitelistController;
import mc.zyntra.general.data.*;
import mc.zyntra.general.data.DataPlayer;
import mc.zyntra.general.data.DataReport;
import mc.zyntra.general.data.DataServer;
import mc.zyntra.general.data.DataStatus;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.mojang.MojangAPI;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

public class Core {

    @Getter
    private static final Gson gson = new Gson();
    @Getter
    private static final MongoConnection mongoStorage = new MongoConnection();
    @Getter
    private static final RedisConnection redisBackend = new RedisConnection();
    @Getter
    private static final JsonParser jsonParser = new JsonParser();
    @Getter
    private static final AccountController accountController = new AccountController();
    @Getter
    private static final FriendController friendController = new FriendController();
    @Getter
    private static final StatisticsController statisticsController = new StatisticsController();
    @Getter
    private static final WhitelistController whitelistController = new WhitelistController();
    @Getter
    private static final ReportController reportController = new ReportController();
    @Getter
    private static final MojangAPI mojangAPI = new MojangAPI();
    @Getter
    @Setter
    private static Logger logger;
    @Getter
    @Setter
    private static DataPlayer dataPlayer;
    @Getter
    @Setter
    private static DataServer dataServer;
    @Getter
    @Setter
    private static DataClan dataClan;
    @Getter
    @Setter
    private static DataStatus dataStatus;
    @Getter
    @Setter
    private static DataReport dataReport;
    @Getter
    @Setter
    private static Platform platform;
    @Getter
    @Setter
    private static String serverName;
    @Getter
    @Setter
    private static ServerType serverType;
}