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
package com.google.devtools.build.lib.skyframe.packages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.bazel.rules.BazelRuleClassProvider;
import com.google.devtools.build.lib.packages.PackageFactory.EnvironmentExtension;
import com.google.devtools.build.lib.packages.RuleClassProvider;
import com.google.devtools.build.lib.runtime.proto.InvocationPolicyOuterClass.InvocationPolicy;
import com.google.devtools.build.lib.skyframe.LocalRepositoryLookupFunction;
import com.google.devtools.build.lib.skyframe.PackageLookupFunction;
import com.google.devtools.build.lib.skyframe.PackageLookupFunction.CrossRepositoryLabelViolationStrategy;
import com.google.devtools.build.lib.skyframe.PackageLookupValue.BuildFileName;
import com.google.devtools.build.lib.skyframe.SkyFunctions;
import com.google.devtools.build.lib.vfs.Path;
import com.google.devtools.build.skyframe.SkyFunction;
import com.google.devtools.build.skyframe.SkyFunctionName;

/**
 * Concrete implementation of {@link PackageLoader} that uses skyframe under the covers, but with
 * no caching or incrementality.
 */
public class BazelPackageLoader extends AbstractPackageLoader {
  /** Returns a fresh {@link Builder} instance. */
  public static Builder builder(Path workspaceDir) {
    return new Builder(workspaceDir);
  }

  /** Builder for {@link BazelPackageLoader} instances. */
  public static class Builder extends AbstractPackageLoader.Builder {
    private Builder(Path workspaceDir) {
      super(workspaceDir);
    }

    @Override
    public BazelPackageLoader build() {
      return new BazelPackageLoader(this);
    }

    @Override
    protected RuleClassProvider getDefaultRuleClassProvider() {
      return BazelRuleClassProvider.create();
    }

    @Override
    protected String getDefaultDefaulsPackageContents() {
      return BazelRuleClassProvider.create().getDefaultsPackageContent(
          InvocationPolicy.getDefaultInstance());
    }
  }

  private BazelPackageLoader(Builder builder) {
    super(builder);
  }

  @Override
  protected String getName() {
    return "BazelPackageLoader";
  }

  @Override
  protected ImmutableList<EnvironmentExtension> getEnvironmentExtensions() {
    return ImmutableList.of();
  }

  @Override
  protected PackageLookupFunction makePackageLookupFunction() {
    return new PackageLookupFunction(
        deletedPackagesRef,
        CrossRepositoryLabelViolationStrategy.ERROR,
        ImmutableList.of(BuildFileName.BUILD_DOT_BAZEL, BuildFileName.BUILD));
  }

  @Override
  protected ImmutableMap<SkyFunctionName, SkyFunction> getExtraExtraSkyFunctions() {
    return ImmutableMap.<SkyFunctionName, SkyFunction>of(
        SkyFunctions.LOCAL_REPOSITORY_LOOKUP, new LocalRepositoryLookupFunction());
    // TODO(nharmata): Add support for external repositories.
  }
}
