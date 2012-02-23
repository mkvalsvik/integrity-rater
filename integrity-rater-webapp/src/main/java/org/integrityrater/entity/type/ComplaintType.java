package org.integrityrater.entity.type;

public enum ComplaintType {
   
    FALSE_OR_MISLEADING("False or misleading", "The advocacy of a position which the advocate knows or believes to be false or misleading"), 
    SLOPPY_WITH_TRUTH("Ignorance or facts not checked", "COMING SOON: The advocacy of a position which the advocate does not know to be true, and has not performed rigorous due diligence to ensure the truthfulness of the position"), 
    OMISSION("Conscious omission", "COMING SOON: The conscious omission of aspects of the truth known or believed to be relevant in the particular context"),
    STRAW_MAN_RESPONSE("Straw man response", "COMING SOON: Create the illusion of having refuted a proposition by replacing it with a superficially similar yet unequivalent proposition");

    private String summary;
    private String title;
    
    private ComplaintType(String title, String summary) {
        this.title = title;
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
    
    public String getTitle() {
        return title;
    }

 }
