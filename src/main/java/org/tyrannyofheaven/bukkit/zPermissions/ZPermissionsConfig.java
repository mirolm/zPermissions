/*
 * Copyright 2013 ZerothAngel <zerothangel@tyrannyofheaven.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tyrannyofheaven.bukkit.zPermissions;

import java.io.File;
import java.util.List;

/**
 * Holds configuration data used by other modules, namely the command handlers.
 *
 * @author zerothangel
 */
public interface ZPermissionsConfig {

    List<String> getTracks();

    String getDefaultTrack();

    List<String> getTrack(String trackName);

    File getDumpDirectory();

    boolean isRankAdminBroadcast();

    int getDefaultTempPermissionTimeout();

    String getDefaultPrimaryGroupTrack();

    boolean isVaultPrefixIncludesGroup();

    boolean isVaultMetadataIncludesGroup();

    boolean isVaultGroupTestUsesAssignedOnly();

    boolean isVaultGetGroupsUsesAssignedOnly();

    boolean isInheritedMetadata();

    String getVaultPlayerPrefixFormat();

    String getVaultPlayerSuffixFormat();

    int getSearchBatchSize();

    int getSearchDelay();

    boolean isServiceMetadataPrefixHack();

}
