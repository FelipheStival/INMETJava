package br.embrapa.cnpaf.inmetdata.period;

import java.time.LocalDate;

public class period {

	private LocalDate start;
	private LocalDate end;

	/**
	 * @param start
	 * @param end
	 */
	public period(LocalDate start, LocalDate end) {
		super();
		this.start = start;
		this.end = end;
	}

	/**
	 * 
	 */
	public period() {
		super();
	}

	/**
	 * @return the start
	 */
	public LocalDate getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(LocalDate start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(LocalDate end) {
		this.end = end;
	}

}
