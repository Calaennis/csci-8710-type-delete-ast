package deleteast.analysis;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import deleteast.model.ProgramElement;
import deleteast.util.UtilAST;
import deleteast.util.UtilPath;
import deleteast.visitor.DeleteMethodVisitor;

public class DeleteMethodAnalyzer {

	private ProgramElement currentProgramElement;

	public DeleteMethodAnalyzer(ProgramElement newProgramName) {
		this.currentProgramElement = newProgramName;

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			try {
				analyzeJavaProject(project);
			} catch (MalformedTreeException | BadLocationException | CoreException e) {
				e.printStackTrace();
			}
		}
	}

	void analyzeJavaProject(IProject project)
			throws CoreException, JavaModelException, MalformedTreeException, BadLocationException {
		if (!project.isOpen() || !project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			return;
		}
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment iPackage : packages) {
			if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE && //
					iPackage.getCompilationUnits().length >= 1 && //
					iPackage.getElementName().equals(currentProgramElement.getPackageName())) {
				deleteMethod(iPackage);
			}
		}
	}

	void deleteMethod(IPackageFragment iPackage)
			throws JavaModelException, MalformedTreeException, BadLocationException {
		for (ICompilationUnit iCUnit : iPackage.getCompilationUnits()) {
			String nameICUnit = UtilPath.getClassNameFromJavaFile(iCUnit.getElementName());
			if (nameICUnit.equals(this.currentProgramElement.getClassName()) == false) {
				continue;
			}
			ICompilationUnit workingCopy = iCUnit.getWorkingCopy(null);
			CompilationUnit cUnit = UtilAST.parse(workingCopy); // Creation of DOM/AST from a ICompilationUnit
			ASTRewrite rewrite = ASTRewrite.create(cUnit.getAST()); // Creation of ASTRewrite
			DeleteMethodVisitor v = new DeleteMethodVisitor(currentProgramElement);
			v.setASTRewrite(rewrite);
			cUnit.accept(v);
			TextEdit edits = rewrite.rewriteAST(); // Compute the edits
			workingCopy.applyTextEdit(edits, null); // Apply the edits.
			workingCopy.commitWorkingCopy(false, null); // Save the changes.
		}
	}

}
