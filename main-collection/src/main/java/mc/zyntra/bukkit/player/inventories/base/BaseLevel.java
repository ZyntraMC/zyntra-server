package mc.zyntra.bukkit.player.inventories.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseLevel {
    LEVEL_0(0, 500),
    LEVEL_1(1, 750),
    LEVEL_2(2, 1000),
    LEVEL_3(3, 1250),
    LEVEL_4(4, 1500),
    LEVEL_5(5, 1750),
    LEVEL_6(6, 2000),
    LEVEL_7(7, 2250),
    LEVEL_8(8, 2500),
    LEVEL_9(9, 2750),
    LEVEL_10(10, 3000),
    LEVEL_11(11, 3250),
    LEVEL_12(12, 3500),
    LEVEL_13(13, 3750),
    LEVEL_14(14, 4000),
    LEVEL_15(15, 4250),
    LEVEL_16(16, 4500),
    LEVEL_17(17, 4750),
    LEVEL_18(18, 5000),
    LEVEL_19(19, 5250),
    LEVEL_20(20, 5500),
    LEVEL_21(21, 5750),
    LEVEL_22(22, 6000),
    LEVEL_23(23, 6250),
    LEVEL_24(24, 6500),
    LEVEL_25(25, 6750),
    LEVEL_26(26, 7000),
    LEVEL_27(27, 7250),
    LEVEL_28(28, 7500),
    LEVEL_29(29, 7750),
    LEVEL_30(30, 8000),
    LEVEL_31(31, 8250),
    LEVEL_32(32, 8500),
    LEVEL_33(33, 8750),
    LEVEL_34(34, 9000),
    LEVEL_35(35, 9250),
    LEVEL_36(36, 9500),
    LEVEL_37(37, 9750),
    LEVEL_38(38, 10000),
    LEVEL_39(39, 10250),
    LEVEL_40(40, 10500),
    LEVEL_41(41, 10750),
    LEVEL_42(42, 11000),
    LEVEL_43(43, 11250),
    LEVEL_44(44, 11500),
    LEVEL_45(45, 11750),
    LEVEL_46(46, 12000),
    LEVEL_47(47, 12250),
    LEVEL_48(48, 12500),
    LEVEL_49(49, 12750),
    LEVEL_50(50, 13000),
    LEVEL_51(51, 13250),
    LEVEL_52(52, 13500),
    LEVEL_53(53, 13750),
    LEVEL_54(54, 14000),
    LEVEL_55(55, 14250),
    LEVEL_56(56, 14500),
    LEVEL_57(57, 14750),
    LEVEL_58(58, 15000),
    LEVEL_59(59, 15250),
    LEVEL_60(60, 15500),
    LEVEL_61(61, 15750),
    LEVEL_62(62, 16000),
    LEVEL_63(63, 16250),
    LEVEL_64(64, 16500),
    LEVEL_65(65, 16750),
    LEVEL_66(66, 17000),
    LEVEL_67(67, 17250),
    LEVEL_68(68, 17500),
    LEVEL_69(69, 17750),
    LEVEL_70(70, 18000),
    LEVEL_71(71, 18250),
    LEVEL_72(72, 18500),
    LEVEL_73(73, 18750),
    LEVEL_74(74, 19000),
    LEVEL_75(75, 19250),
    LEVEL_76(76, 19500),
    LEVEL_77(77, 19750),
    LEVEL_78(78, 20000),
    LEVEL_79(79, 20250),
    LEVEL_80(80, 20500),
    LEVEL_81(81, 20750),
    LEVEL_82(82, 21000),
    LEVEL_83(83, 21250),
    LEVEL_84(84, 21500),
    LEVEL_85(85, 21750),
    LEVEL_86(86, 22000),
    LEVEL_87(87, 22250),
    LEVEL_88(88, 22500),
    LEVEL_89(89, 22750),
    LEVEL_90(90, 23000),
    LEVEL_91(91, 23250),
    LEVEL_92(92, 23500),
    LEVEL_93(93, 23750),
    LEVEL_94(94, 24000),
    LEVEL_95(95, 24250),
    LEVEL_96(96, 24500),
    LEVEL_97(97, 24750),
    LEVEL_98(98, 25000),
    LEVEL_99(99, 25250),
    LEVEL_100(100, 25500);

    final int numberLevel;
    final int xpToUp;

    public static BaseLevel getLevelByNumber(int numberLevel) {
        for (BaseLevel level : values()) {
            if (level.numberLevel == numberLevel) {
                return level;
            }
        }
        throw new IllegalArgumentException("Nível não encontrado para o número fornecido: " + numberLevel);
    }

    public static String getProgressBar(int level, int exp) {
        int nextLevelExp = getLevelByNumber(level).getXpToUp();

        double progress = (double) exp / nextLevelExp;
        int progressBarLength = 10;

        int filledSquares = (int) (progress * progressBarLength);

        StringBuilder progressBar = new StringBuilder("§a■︎");
        for (int i = 0; i < progressBarLength; i++) {
            if (i < filledSquares) {
                progressBar.append("§a■");
            } else {
                progressBar.append("§7□");
            }
        }

        return progressBar.toString();
    }
}
