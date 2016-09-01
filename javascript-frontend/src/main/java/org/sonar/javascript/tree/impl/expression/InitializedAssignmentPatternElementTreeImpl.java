package org.sonar.javascript.tree.impl.expression;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.InitializedAssignmentPatternElementTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitor;

import static org.sonar.plugins.javascript.api.tree.Tree.Kind.INITIALIZED_ASSIGNMENT_PATTERN_ELEMENT;

public class InitializedAssignmentPatternElementTreeImpl extends JavaScriptTree implements InitializedAssignmentPatternElementTree {

  private final ExpressionTree left;
  private final SyntaxToken equalToken;
  private final ExpressionTree right;

  public InitializedAssignmentPatternElementTreeImpl(ExpressionTree left, SyntaxToken equalToken, ExpressionTree right) {
    this.left = left;
    this.equalToken = equalToken;
    this.right = right;
  }

  @Override
  public Kind getKind() {
    return INITIALIZED_ASSIGNMENT_PATTERN_ELEMENT;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(left, equalToken, right);
  }

  @Override
  public ExpressionTree left() {
    return left;
  }

  @Override
  public SyntaxToken equalToken() {
    return equalToken;
  }

  @Override
  public ExpressionTree right() {
    return right;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitInitializedAssignmentPatternElement(this);
  }
}
