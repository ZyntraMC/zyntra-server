package mc.zyntra.general.utils.string;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    public static long fromString(String str) {
        String[] arrayOfString;
        String[] timeString = str.split(",");

        int templateInteger;
        int integer;
        int day = 0, hour = 0, minute = 0, second = 0;

        templateInteger = (arrayOfString = timeString).length;
        for (integer = 0; integer < templateInteger; integer++) {
            String string = arrayOfString[integer];
            if (string.contains("d") || string.contains("D")) {
                day = Integer.parseInt(string.replace("d", "").replace("D", ""));
            }
            if (string.contains("h") || string.contains("H")) {
                hour = Integer.parseInt(string.replace("h", "").replace("H", ""));
            }
            if (string.contains("m") || string.contains("M")) {
                minute = Integer.parseInt(string.replace("m", "").replace("M", ""));
            }
            if (string.contains("s") || string.contains("S")) {
                second = Integer.parseInt(string.replace("s", "").replace("S", ""));
            }
        }
        return convert(day, hour, minute, second);
    }

    public static String toLongString(long time) {
        String message = "";
        long now = System.currentTimeMillis();
        long diff = time - now;
        int seconds = (int) (diff / 1000L);
        if (seconds >= 86400) {
            int days = seconds / 86400;
            seconds %= 86400;
            if (days == 1) {
                message = message + days + " dia ";
            } else {
                message = message + days + " dias ";
            }
        }
        if (seconds >= 3600) {
            int hours = seconds / 3600;
            seconds %= 3600;
            if (hours == 1) {
                message = message + hours + " hora ";
            } else {
                message = message + hours + " horas ";
            }
        }
        if (seconds >= 60) {
            int min = seconds / 60;
            seconds %= 60;
            if (min == 1) {
                message = message + min + " minuto ";
            } else {
                message = message + min + " minutos ";
            }
        }
        if (seconds >= 0) {
            if (seconds == 1) {
                message = message + seconds + " segundo ";
            } else {
                message = message + seconds + " segundos ";
            }
        }
        if (seconds < 0) {
            message = "§6Vitalício";
        }
        return message;
    }

    public static String convertTimeToString(long time) {
        StringBuilder builder = new StringBuilder();

        long now = System.currentTimeMillis();
        long diff = time - now;
        int seconds = (int) (diff / 1000L);

        /* Days */
        if (seconds >= 86400) {
            int days = seconds / 86400;
            seconds %= 86400;
            builder.append(days).append(" ").append("dia").append(days > 1 ? "s" : "");

            if (seconds >= 3600) {
                int hours = seconds / 3600;
                seconds %= 3600;
                builder.append(" e ").append(hours).append(" ").append("hora").append(hours > 1 ? "s" : "");
            }

            return builder.toString();
        }

        /* Hours */
        if (seconds >= 3600) {
            int hours = seconds / 3600;
            seconds %= 3600;
            builder.append(hours).append(" ").append("hora").append(hours > 1 ? "s" : "");

            if (seconds >= 60) {
                int min = seconds / 60;
                seconds %= 60;
                builder.append(" e ").append(min).append(" ").append("minuto").append(min > 1 ? "s" : "");
            }

            return builder.toString();
        }

        /* Minutes */
        if (seconds >= 60) {
            int min = seconds / 60;
            seconds %= 60;
            builder.append(min).append(" ").append("minuto").append(min > 1 ? "s" : "");
            builder.append(" e ").append(seconds).append(" ").append("segundo").append(seconds > 1 ? "s" : "");

            return builder.toString();
        }

        /* Seconds */
        if (seconds >= 0) {
            builder.append(seconds).append(" ").append("segundo").append(seconds > 1 ? "s" : "");
            return builder.toString();
        }

        return "";
    }

    public static String toString(long time) {
        String message = "";
        long now = System.currentTimeMillis();
        long diff = time - now;
        int seconds = (int) (diff / 1000L);
        if (seconds >= 86400) {
            int days = seconds / 86400;
            seconds %= 86400;
            message = message + days + " dia" + (days > 1 ? "s" : "");
            return message;
        }
        if (seconds >= 3600) {
            int hours = seconds / 3600;
            seconds %= 3600;
            message = message + hours + " hora" + (hours > 1 ? "s" : "");
            return message;
        }
        if (seconds >= 60) {
            int min = seconds / 60;
            seconds %= 60;
            message = message + min + " minuto" + (min > 1 ? "s" : "");
            return message;
        }
        if (seconds >= 0) {
            message = message + seconds + " segundo" + (seconds > 1 ? "s" : "");
            return message;
        }
        return message;
    }

    public static String formatTime(long time) {
        if (time <= 0) return "";

        double seconds = (double) (time - System.currentTimeMillis()) / 1000;
        StringBuilder sb = new StringBuilder();

        if (seconds >= 60) {
            double minutes = seconds / 60;

            sb.append(new DecimalFormat("#.#").format(minutes)).append(" ");
            sb.append("minuto").append(minutes > 1 ? "s" : "");
            return sb.toString();
        } else {
            sb.append(new DecimalFormat("#.#").format(seconds)).append(" ");
            sb.append("segundo").append(seconds > 1 ? "s" : "");
        }

        return sb.toString();
    }

    public static String formatCut(long time, boolean all) {
        if (time <= 0) {
            return "";
        }

        long day = TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - (day * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time) * 60);
        long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) * 60);

        StringBuilder sb = new StringBuilder();

        int i = 0;
        if (day > 0) {
            sb.append(day).append(all ? " " : "").append("d").append(all ? (day > 1 ? "ias" : "ia") : "").append(" ");
            i++;
        }
        if (hours > 0) {
            sb.append(hours).append(all ? " " : "").append("h").append(all ? (hours > 1 ? "oras" : "ora") : "").append(" ");
            i++;
        }
        if (i < 2)
            if (minutes > 0) {
                sb.append(minutes).append(all ? " " : "").append("m").append(all ? (minutes > 1 ? "inutos" : "inuto") : "").append(" ");
                i++;
            }
        if (i < 2)
            if (seconds > 0) {
                sb.append(seconds).append(all ? " " : "").append("s").append(all ? (seconds > 1 ? "egundos" : "egundo") : "");
                i++;
            }

        return sb.toString();
    }

    public static String getPassedTime(long time) {
        String message = "";

        long now = System.currentTimeMillis();
        long diff = now - time;
        int seconds = (int) (diff / 1000L);

        if (seconds >= 86400) {
            int days = seconds / 86400;
            seconds %= 86400;
            message = message + days + " dia" + (days > 1 ? "s" : "");
            return message;
        }
        if (seconds >= 3600) {
            int hours = seconds / 3600;
            seconds %= 3600;
            message = message + (seconds >= 86400 ? " e " : "") + hours + " hora" + (hours > 1 ? "s" : "");
            return message;
        }
        if (seconds >= 60) {
            int min = seconds / 60;
            seconds %= 60;
            message = message + min + " minuto" + (min > 1 ? "s" : "");
            //return message;
        }
        if (seconds >= 0) {
            message = message + (seconds >= 60 ? " e " : "") + seconds + " segundo" + (seconds > 1 ? "s" : "");
            return message;
        }
        return message;
    }

    protected static long convert(long days, long hours, long minutes, long seconds) {
        long x;
        long minute = 60L * minutes;
        long hour = 3600L * hours;
        long day = 86400L * days;

        x = minute + hour + day + seconds;
        return System.currentTimeMillis() + x * 1000L;
    }

    public static long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile(
                "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
                2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if ((m.group() != null) && (!m.group().isEmpty())) {
                for (int i = 0; i < m.groupCount(); i++) {
                    if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
                        years = Integer.parseInt(m.group(1));
                    }
                    if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
                        months = Integer.parseInt(m.group(2));
                    }
                    if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
                        weeks = Integer.parseInt(m.group(3));
                    }
                    if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
                        days = Integer.parseInt(m.group(4));
                    }
                    if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
                        hours = Integer.parseInt(m.group(5));
                    }
                    if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
                        minutes = Integer.parseInt(m.group(6));
                    }
                    if ((m.group(7) == null) || (m.group(7).isEmpty())) {
                        break;
                    }
                    seconds = Integer.parseInt(m.group(7));

                    break;
                }
            }
        }
        if (!found) {
            throw new Exception("Illegal Date");
        }
        if (years > 20) {
            throw new Exception("Illegal Date");
        }
        Calendar c = new GregorianCalendar();
        if (years > 0) {
            c.add(1, years * (future ? 1 : -1));
        }
        if (months > 0) {
            c.add(2, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            c.add(3, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            c.add(5, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            c.add(11, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            c.add(12, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            c.add(13, seconds * (future ? 1 : -1));
        }
        return c.getTimeInMillis();
    }
}
