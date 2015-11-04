package com.github.tgiachi.ares.interfaces.fs;

import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.TemplateResult;
import com.github.tgiachi.ares.interfaces.managers.IAresManager;

/**
 * Interfaccia per creare il manager del file system.
 */
public interface IFileSystemManager extends IAresManager {

    TemplateResult getTemplate(String filename, DataModel model);

    void shutdown();


}
