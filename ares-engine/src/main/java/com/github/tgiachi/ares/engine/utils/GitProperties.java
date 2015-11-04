package com.github.tgiachi.ares.engine.utils;

import java.util.Properties;

/**
 * Prende le informazioni su la versione git
 */
public class GitProperties {


    private Properties mGitProperties;

    public GitProperties()
    {
        mGitProperties = new Properties();
        try
        {
            mGitProperties.load(getClass().getResourceAsStream("/resources/git.properties"));
        }
        catch (Exception ex)
        {

        }
    }

    public String getCommitId()
    {
        return mGitProperties.getProperty("git.commit.id");
    }

    public String getCommitIdAbbrev(){
        return mGitProperties.getProperty("git.commit.id.abbrev");
    }

    public String getBranch(){
        return mGitProperties.getProperty("git.branch");
    }
}
