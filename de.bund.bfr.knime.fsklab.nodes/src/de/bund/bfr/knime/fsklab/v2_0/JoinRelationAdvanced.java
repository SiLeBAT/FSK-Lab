package de.bund.bfr.knime.fsklab.v2_0;

public class JoinRelationAdvanced extends JoinRelation {

  private final FskPortObject fskObj;
  
  public FskPortObject getModel() {
    return fskObj;
  }
  
  public JoinRelationAdvanced(JoinRelation joinRelation, FskPortObject fskObj, String suffix) {
    
    super( joinRelation.getSourceParam(),
        joinRelation.getTargetParam(),
        joinRelation.getCommand(),
        joinRelation.getLanguage_written_in());
    
    this.fskObj = fskObj;
  
  }
  
  
  public String replaceCommand(String parameter) {
    return super.getCommand().replaceAll("\\[([^<]*)\\]", parameter);
  }
  
  
  
}
