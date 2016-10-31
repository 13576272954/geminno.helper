package com.example.administrator.helper.entity;

public class ShareEntity {
	 private Dynamic dynamic;
	    private boolean isCheck;

	    public ShareEntity(Dynamic dynamic, boolean isCheck) {
	        this.dynamic = dynamic;
	        this.isCheck = isCheck;
	    }

	    public Dynamic getDynamic() {
	        return dynamic;
	    }

	    public void setDynamic(Dynamic dynamic) {
	        this.dynamic = dynamic;
	    }

	    public boolean isCheck() {
	        return isCheck;
	    }

	    public void setCheck(boolean check) {
	        isCheck = check;
	    }
}
