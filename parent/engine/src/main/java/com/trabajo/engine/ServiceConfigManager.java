package com.trabajo.engine;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.ServiceInfo;

public interface ServiceConfigManager {
    ServiceInfo getServiceInfo(DefinitionVersion dv);
}
