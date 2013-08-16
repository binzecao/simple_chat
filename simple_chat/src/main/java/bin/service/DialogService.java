package bin.service;

import java.util.List;

import bin.model.Dialog;

public interface DialogService {
	public boolean save(Dialog dialog) throws Throwable;
	public List<Dialog> getAll() throws Throwable;
	public Dialog getSingle(int id) throws Throwable;
	public Dialog getFirst() throws Throwable;
	public int getMaxId() throws Throwable;
	public boolean removeAll() throws Throwable;
}
