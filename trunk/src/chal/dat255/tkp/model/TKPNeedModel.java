package chal.dat255.tkp.model;

public enum TKPNeedModel {
	//TODO , System.currentTimeMillis() should not be here...
	Hunger(Level.None, 0, 4*60*1000, System.currentTimeMillis() ),
	Sleep(Level.None, 0, 4*60*1000, System.currentTimeMillis() ); //4*60*1000 Four minutes 
	
	enum Level {
		None, 
		Normal,
		More,
		Very,
		Critical
	}

	Level level = Level.None;
	private int needLevel = 0;
	private long lastUpdate;
	private long updateIntervall;
	
	TKPNeedModel(Level level, int needLevel, long updateInterall, long lastUpdate) {
		this.level=level;
		this.needLevel = needLevel;
		this.updateIntervall = updateInterall;
		this.lastUpdate = lastUpdate;
	}

	public void increaseNeedLevel(int amount) {
		if (amount > 0) {
			if ((amount + needLevel) > 100) {
				needLevel = 100;
			} else {
				needLevel += amount;
			}
			checkNeedLevel();
		}
	}

	public void decreaseNeedLevel(int amount) {
		if (amount > 0) {
			if ((amount - needLevel) > 0) {
				needLevel -= amount;
			} else {
				needLevel = 0;
			}
			checkNeedLevel();
		}
	}

	public void resetNeedLevel() {
		needLevel = 0;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public long getUpdateIntervall() {
		return updateIntervall;
	}

	public Level getLevel(){
		return this.level;
	}
	
	private void setLevel(Level level) {
		this.level = level;
	}
	
	private void checkNeedLevel() {
		if (needLevel > 80) {
			setLevel(Level.Critical);
		} else if ( needLevel > 60 ) {
			setLevel(Level.Very);
		} else if ( needLevel > 40 ) {
			setLevel(Level.More);
		}  else if ( needLevel > 20 ) {
			setLevel(Level.Normal);
		}  else {
			setLevel(Level.None);
		}
	}
}

