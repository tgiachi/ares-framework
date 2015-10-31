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

    /**
     * git.commit.id=23cecb929c0229f3e5fbf56b9dd503bd124b5d95
     git.commit.time=1446223712
     git.commit.user.name=Tommaso Giachi
     git.commit.id.abbrev=23cecb9
     git.branch=develop
     git.commit.message.short=Aggiunto il container per i beans
     git.commit.user.email=squid@stormwind.it
     git.commit.message.full=Aggiunto il container per i beans\n

     */
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
