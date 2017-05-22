// Copyright 2016 The Bazel Authors. All rights reserved.
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
package com.google.devtools.build.android.resources;

import com.google.common.base.MoreObjects;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Objects;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.InstructionAdapter;

/**
 * Models an int field initializer.
 */
public final class IntFieldInitializer implements FieldInitializer {

  private final String fieldName;
  private final int value;
  private static final String DESC = "I";

  public IntFieldInitializer(String fieldName, int value) {
    this.fieldName = fieldName;
    this.value = value;
  }

  public static FieldInitializer of(String name, String value) {
    return new IntFieldInitializer(name, Integer.decode(value));
  }

  @Override
  public boolean writeFieldDefinition(ClassWriter cw, int accessLevel, boolean isFinal) {
    cw.visitField(accessLevel, fieldName, DESC, null, isFinal ? value : null)
        .visitEnd();
    return !isFinal;
  }

  @Override
  public int writeCLInit(InstructionAdapter insts, String className) {
    insts.iconst(value);
    insts.putstatic(className, fieldName, DESC);
    // Just needs one stack slot for the iconst.
    return 1;
  }

  @Override
  public void writeInitSource(Writer writer, boolean finalFields) throws IOException {
    writer.write(
        String.format(
            "        public static %sint %s = 0x%x;\n",
            finalFields ? "final " : "", fieldName, value));
  }

  @Override
  public boolean nameIsIn(Collection<String> fieldNames) {
    return fieldNames.contains(fieldName);
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass())
        .add("fieldName", fieldName)
        .add("value", value)
        .toString();
  }
  
  @Override
  public int compareTo(FieldInitializer other) {
    if (other instanceof IntFieldInitializer) {
      return fieldName.compareTo(((IntFieldInitializer) other).fieldName);
    }
    // IntFields will go before Intarrays
    return -1;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldName, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IntFieldInitializer) {
      IntFieldInitializer other = (IntFieldInitializer) obj;
      return Objects.equals(fieldName, other.fieldName) && value == other.value;
    }
    return false;
  }

  @Override
  public void addTo(Collection<String> fieldNames) {
    fieldNames.add(fieldName);
  }
}
