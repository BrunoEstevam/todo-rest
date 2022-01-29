package br.com.viceri.todo.model;

public enum Status {

	OPENED("A"), COMPLETED("M");

	private Status(String codigo) {
		this.codigo = codigo;
	}

	private final String codigo;

	public String getCodigo() {
		return codigo;
	}

	public static Status valueOfCodigo(String codigo) {
		for (Status codigoEnum : Status.values()) {
			if (codigoEnum.getCodigo().equals(codigo)) {
				return codigoEnum;
			}
		}

		return null;
	}
}
