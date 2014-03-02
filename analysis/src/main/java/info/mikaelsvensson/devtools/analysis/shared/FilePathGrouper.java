/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.analysis.shared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

enum FilePathGrouper
{
    NONE
            {
                @Override
                File[][] groupFiles(String[] paths)
                {
                    File[][] fileSets;
                    int fileCount = paths.length;

                    fileSets = new File[fileCount][1];
                    for (int i = 0; i < paths.length; i++)
                    {
                        String filePath = paths[i];
                        fileSets[i] = new File[]{new File(filePath)};
                    }
                    return fileSets;
                }
            },
    MERGED
            {
                @Override
                File[][] groupFiles(String[] paths)
                {
                    File[][] fileSets;
                    int fileCount = paths.length;

                    fileSets = new File[1][];
                    fileSets[0] = new File[fileCount];
                    for (int i = 0; i < paths.length; i++)
                    {
                        String filePath = paths[i];
                        fileSets[0][i] = new File(filePath);
                    }
                    return fileSets;
                }
            },
    MERGE_BY_FOLDER
            {
                @Override
                File[][] groupFiles(String[] paths)
                {
                    Map<String, List<File>> groups = new TreeMap<String, List<File>>();
                    for (String path : paths)
                    {
                        final File f = new File(path);
                        if (!groups.containsKey(f.getParent()))
                        {
                            groups.put(f.getParent(), new ArrayList<File>());
                        }
                        groups.get(f.getParent()).add(f);
                    }
                    File[][] fileSets = new File[groups.size()][];
                    int i = 0;
                    for (List<File> files : groups.values())
                    {
                        fileSets[i++] = files.toArray(new File[files.size()]);
                    }
                    return fileSets;
                }
            };

    abstract File[][] groupFiles(String[] paths);
}
