package funs.tools.template;

import java.util.Collections;
import java.util.List;

public class TemplateSheetConfig {

	private String mvl;

	private String output;

	private List<String> profiles;

	public String getMvl() {
		return mvl;
	}

	public void setMvl(String mvl) {
		this.mvl = mvl;
	}

	public String getOutput() {
		return output;
	}

	public List<String> getProfiles() {
		if (profiles == null)
			return Collections.emptyList();
		return profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
