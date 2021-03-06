/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.tree.impl.declaration;

import org.junit.Test;
import org.sonar.javascript.utils.JavaScriptTreeModelTest;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.declaration.FieldDeclarationTree;
import org.sonar.plugins.javascript.api.tree.expression.ClassTree;
import org.sonar.plugins.javascript.api.tree.expression.IdentifierTree;

import static org.fest.assertions.Assertions.assertThat;

public class ClassDeclarationTreeModelTest extends JavaScriptTreeModelTest {

  @Test
  public void without_members() throws Exception {
    ClassTree tree = parse("@decorator class C { }", Kind.CLASS_DECLARATION);

    assertThat(tree.is(Kind.CLASS_DECLARATION)).isTrue();
    assertThat(tree.decorators()).hasSize(1);
    assertThat(tree.classToken().text()).isEqualTo("class");
    assertThat(tree.name().name()).isEqualTo("C");
    assertThat(tree.extendsToken()).isNull();
    assertThat(tree.superClass()).isNull();
    assertThat(tree.openCurlyBraceToken().text()).isEqualTo("{");
    assertThat(tree.elements()).isEmpty();
    assertThat(tree.methods()).isEmpty();
    assertThat(tree.semicolons()).isEmpty();
    assertThat(tree.closeCurlyBraceToken().text()).isEqualTo("}");
  }

  @Test
  public void with_members() throws Exception {
    ClassTree tree = parse("class C { m() {} static m(){} ; }", Kind.CLASS_DECLARATION);

    assertThat(tree.is(Kind.CLASS_DECLARATION)).isTrue();
    assertThat(tree.classToken().text()).isEqualTo("class");
    assertThat(tree.name().name()).isEqualTo("C");
    assertThat(tree.extendsToken()).isNull();
    assertThat(tree.superClass()).isNull();
    assertThat(tree.openCurlyBraceToken().text()).isEqualTo("{");
    assertThat(tree.elements()).hasSize(3);
    assertThat(tree.methods()).hasSize(2);
    assertThat(tree.semicolons()).hasSize(1);
    assertThat(tree.closeCurlyBraceToken().text()).isEqualTo("}");
  }

  @Test
  public void extends_clause() throws Exception {
    ClassTree tree = parse("class C extends S { }", Kind.CLASS_DECLARATION);

    assertThat(tree.extendsToken().text()).isEqualTo("extends");
    assertThat(tree.superClass()).isNotNull();
  }

  @Test
  public void not_global_class_should_be_declaration() throws Exception {
    ClassTree tree = parse("if (true) { class A{} }", Kind.CLASS_DECLARATION);

    assertThat(tree.is(Kind.CLASS_DECLARATION)).isTrue();
    assertThat(tree.name().name()).isEqualTo("A");
  }

  @Test
  public void property() throws Exception {
    ClassTree tree = parse("class A { static staticProperty = 1; method(){} withoutInitField; }", Kind.CLASS_DECLARATION);

    FieldDeclarationTree staticProperty = (FieldDeclarationTree) tree.elements().get(0);
    assertThat(staticProperty.staticToken().text()).isEqualTo("static");
    assertThat(((IdentifierTree) staticProperty.propertyName()).name()).isEqualTo("staticProperty");
    assertThat(staticProperty.equalToken().text()).isEqualTo("=");
    assertThat(staticProperty.initializer().is(Kind.NUMERIC_LITERAL)).isTrue();
    assertThat(staticProperty.semicolonToken().text()).isEqualTo(";");

    FieldDeclarationTree withoutInitField = (FieldDeclarationTree) tree.elements().get(2);
    assertThat(withoutInitField.staticToken()).isNull();
    assertThat(((IdentifierTree) withoutInitField.propertyName()).name()).isEqualTo("withoutInitField");
    assertThat(withoutInitField.equalToken()).isNull();
    assertThat(withoutInitField.initializer()).isNull();
    assertThat(withoutInitField.semicolonToken().text()).isEqualTo(";");


  }
}
