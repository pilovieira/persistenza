package br.com.pilovieira.persistenza;

public class OptionalConfigs {
	
	private boolean autoDecorate = true;
	private boolean idAutoGenerated = true;

	public boolean isAutoDecorate() {
		return autoDecorate;
	}

	public void setAutoDecorate(boolean autoDecorate) {
		this.autoDecorate = autoDecorate;
	}

	public boolean isIdAutoGenerated() {
		return idAutoGenerated;
	}

	public void setIdAutoGenerated(boolean idAutoGenerated) {
		this.idAutoGenerated = idAutoGenerated;
	}

}
