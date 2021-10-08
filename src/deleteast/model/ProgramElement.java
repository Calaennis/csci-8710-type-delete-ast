package deleteast.model;

public class ProgramElement {
	private String	packageName;
	private String	className;
	private String	methodName;
	private boolean	isReturnVoid;
	private Integer	parameterSize;

	public ProgramElement() {
	}

	public ProgramElement(
		String packageName, 
		String className, 
		String methodName, 
		boolean isReturnVoid, 
		int parameterSize
	) {
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		this.isReturnVoid = isReturnVoid;
		this.parameterSize = parameterSize;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isReturnVoid() {
		return isReturnVoid;
	}

	public void setReturnVoid(boolean isReturnVoid) {
		this.isReturnVoid = isReturnVoid;
	}

	public Integer getParameterSize() {
		return parameterSize;
	}

	public void setParameterSize(Integer parameterSize) {
		this.parameterSize = parameterSize;
	}

	@Override
	public String toString() {
		return packageName + "." + className + "." + methodName;
	}
}
