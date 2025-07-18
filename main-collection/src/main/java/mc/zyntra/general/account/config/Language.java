package mc.zyntra.general.account.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {

    PT_BR("Português, Brásil", "America Do Sul, Brazil"),
    EN_US("Inglês, Estados Únidos", "North America, United States"),
    PT_EU("Português, Portugal", "Europa, Portugal");

    private String name, country;
}
