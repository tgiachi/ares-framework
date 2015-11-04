package com.github.tgiachi.ares.sessions;

import lombok.Data;

/**
 * Classe statica per salvare le directory in sessione
 */
@Data
public class DirectoriesConfig {

    private String rootDirectory;
    private String appDirectory;
    private String appConfigDirectory;
    private String templateDirectory;
    private String appRoutesDirectory;
    private String appDatabaseDirectory;
    private String viewsDirectory;



}
