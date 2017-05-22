// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.analysis;

import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.analysis.platform.ToolchainInfo;
import com.google.devtools.build.lib.packages.ClassObjectConstructor;
import com.google.devtools.build.lib.syntax.SkylarkDict;
import java.util.Map;
import javax.annotation.Nullable;

/** Contains toolchain-related information needed for a {@link RuleContext}. */
public class ToolchainContext {
  private final ImmutableMap<ClassObjectConstructor.Key, ToolchainInfo> toolchains;

  public ToolchainContext(@Nullable Map<ClassObjectConstructor.Key, ToolchainInfo> toolchains) {
    this.toolchains =
        toolchains == null
            ? ImmutableMap.<ClassObjectConstructor.Key, ToolchainInfo>of()
            : ImmutableMap.copyOf(toolchains);
  }

  public SkylarkDict<ClassObjectConstructor.Key, ToolchainInfo> collectToolchains() {
    return SkylarkDict.<ClassObjectConstructor.Key, ToolchainInfo>copyOf(null, toolchains);
  }
}
