package deleteast.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import deleteast.model.ModelProvider;

public class DeclarationVisitor extends ASTVisitor {
	private String packageName;
	private String className;
	private String methodName;

	public DeclarationVisitor() {
	}

	@Override
	public boolean visit(PackageDeclaration pkgDecl) {
		packageName = pkgDecl.getName().getFullyQualifiedName();
		return super.visit(pkgDecl);
	}

	/**
	 * A type declaration is the union of a class declaration and an interface
	 * declaration.
	 */
	@Override
	public boolean visit(TypeDeclaration typeDecl) {
		className = typeDecl.getName().getIdentifier();
		return super.visit(typeDecl);
	}

	@Override
	public boolean visit(MethodDeclaration methodDecl) {
		methodName = methodDecl.getName().getIdentifier();
		Type returnType = methodDecl.getReturnType2();
		boolean isReturnVoid = false;
		if (returnType.isPrimitiveType()) {
			PrimitiveType pt = (PrimitiveType) returnType;
			if (pt.getPrimitiveTypeCode().equals(PrimitiveType.VOID)) {
				isReturnVoid = true;
			}
		}
		int parameterSize = methodDecl.parameters().size();
		ModelProvider.INSTANCE.addProgramElements(packageName, className, methodName, isReturnVoid, parameterSize);
		return super.visit(methodDecl);
	}
}
