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

package com.google.devtools.build.lib.runtime;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParser.OptionUsageRestrictions;

/**
 * Common options for authentication and TLS.
 */
public class AuthAndTLSOptions extends OptionsBase {
  @Option(
      name = "auth_enabled",
      defaultValue = "false",
      category = "remote",
      help = "Whether to enable authentication for remote execution/caching and the build event "
          + "service (BES). If not otherwise specified 'Google Application Default Credentials' "
          + "are used. Disabled by default."
  )
  public boolean authEnabled;

  @Option(
      name = "auth_scope",
      defaultValue = "null",
      category = "remote",
      help = "If server authentication requires a scope, provide it here."
  )
  public String authScope;

  @Option(
      name = "auth_credentials",
      defaultValue = "null",
      category = "remote",
      help = "Specifies the file to get authentication credentials from. See "
          + "https://cloud.google.com/docs/authentication for more details. 'Google Application "
          + "Default Credentials' are used by default."
  )
  public String authCredentials;

  @Option(
      name = "tls_enabled",
      defaultValue = "false",
      category = "remote",
      help = "Specifies whether to use TLS for remote execution/caching and the build event service"
          + " (BES)."
  )
  public boolean tlsEnabled;

  @Option(
      name = "tls_certificate",
      defaultValue = "null",
      category = "remote",
      help = "Specify the TLS client certificate to use."
  )
  public String tlsCertificate;

  @Option(
      name = "tls_authority_override",
      defaultValue = "null",
      category = "remote",
      optionUsageRestrictions = OptionUsageRestrictions.HIDDEN,
      help = "TESTING ONLY! Can be used with a self-signed certificate to consider the specified "
          + "value a valid TLS authority."
  )
  public String tlsAuthorityOverride;
}
