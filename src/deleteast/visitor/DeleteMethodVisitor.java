package deleteast.visitor;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import deleteast.model.ProgramElement;

public class DeleteMethodVisitor extends ASTVisitor {
	private ProgramElement programElementToBeRemoved;
	private MethodDeclaration methodToBeRemoved;
	private ASTRewrite rewrite;

	@Inject
	private Shell shell;

	public DeleteMethodVisitor(ProgramElement currentProgramElement) {
		this.programElementToBeRemoved = currentProgramElement;
	}

	public void setASTRewrite(ASTRewrite rewrite) {
		this.rewrite = rewrite;
	}

	@Override
	public void endVisit(TypeDeclaration typeDecl) {
		ListRewrite listRewrite = rewrite.getListRewrite(typeDecl, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.remove(methodToBeRemoved, null);
	}

	public boolean visit(MethodDeclaration node) {
		String name = node.getName().getFullyQualifiedName();
		if (name.equals(programElementToBeRemoved.getMethodName())) {
			int methodModifiers = node.getModifiers();
			boolean isPrivate = (methodModifiers & Modifier.PRIVATE) != 0;
			
			if (isPrivate) {
				MessageDialog.openInformation(shell, "Delete Method", "Deleting " + node.getName());
				this.methodToBeRemoved = node;
				return false;
			} else {
				MessageDialog.openWarning(shell, "Delete Method", "Can not delete the selected method " +
					node.getName() + " because it is not a private method.");
			}
		}
		return true;
	}

}
