package deleteast.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelProvider {
	INSTANCE;

	private List<ProgramElement> programElements = new ArrayList<ProgramElement>();;

	private ModelProvider() {}

	public void addProgramElements(
		String packageName,
		String className,
		String methodName,
		boolean isReturnVoid, 
		int parameterSize
	) {
		programElements.add(
			new ProgramElement(
				packageName,
				className,
				methodName,
				isReturnVoid,
				parameterSize));
	}

	public List<ProgramElement> getProgramElements() {
		return programElements;
	}

	public void clearProgramElements() {
		programElements.clear();
	}
}
