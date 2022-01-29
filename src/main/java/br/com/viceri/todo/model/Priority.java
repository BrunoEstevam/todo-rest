package br.com.viceri.todo.model;

public enum Priority {

	ALTA("A"), MEDIA("M"), BAIXA("B");

	private Priority(String codigo) {
		this.codigo = codigo;
	}

	private final String codigo;

	public String getCodigo() {
		return codigo;
	}

	public static Priority valueOfCodigo(String codigo) {
		for (Priority codigoEnum : Priority.values()) {
			if (codigoEnum.getCodigo().equals(codigo)) {
				return codigoEnum;
			}
		}

		return null;
	}
}
