package com.github.tgiachi.ares.sessions;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe statica per salvare le directory in sessione
 */
public class DirectoriesConfig {


    @Setter @Getter
    private static String rootDirectory;

    @Getter @Setter
    private static String appDirectory;

    @Getter @Setter
    private static String appConfigDirectory;

    @Getter @Setter
    private static String templateDirectory;

}
