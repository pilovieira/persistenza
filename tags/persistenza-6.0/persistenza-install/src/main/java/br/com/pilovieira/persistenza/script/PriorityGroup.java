package br.com.pilovieira.persistenza.script;



class PriorityGroup implements Comparable<PriorityGroup> {
	
	private Integer index;
	private ScriptGroup group;

	public PriorityGroup(int index, ScriptGroup group) {
		this.index = index;
		this.group = group;
	}

	public ScriptGroup getGroup() {
		return group;
	}
	
	@Override
	public int compareTo(PriorityGroup o) {
		return index.compareTo(o.index);
	}
}
