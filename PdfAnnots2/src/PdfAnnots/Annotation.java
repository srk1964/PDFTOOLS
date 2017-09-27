package PdfAnnots;

public class Annotation {
    private String pdfpage;
    private String accounttype;
    private String vsaccount;
    private String name;
    private String date;
    private String docpage;
    private String doctotalpages;
    private String currentname = "";

    public void Annotation(String annot) {
        String[] splitter1 = annot.split(",");
        //System.out.println("Fields="+splitter1.length);
        //if there are 8 fields there is likely a comma in the name field
        if(splitter1.length==7){
            this.pdfpage = splitter1[0].split("=")[1];
            this.accounttype = splitter1[1].split("=")[1];
            this.vsaccount = splitter1[2].split("=")[1];
            this.name = splitter1[3].split("=")[1].trim();
            this.date = splitter1[4].split("=")[1];
            this.docpage = splitter1[5].split("=")[1];
            this.doctotalpages = splitter1[6].split("=")[1];
        }else if(splitter1.length==8){
            this.pdfpage = splitter1[0].split("=")[1];
            this.accounttype = splitter1[1].split("=")[1];
            this.vsaccount = splitter1[2].split("=")[1];
            this.name = splitter1[3].split("=")[1].trim() +", "+ splitter1[4].trim();
            this.date = splitter1[5].split("=")[1];
            this.docpage = splitter1[6].split("=")[1];
            this.doctotalpages = splitter1[7].split("=")[1];
        }
  
    }  
    public void setCurrentName(String currentname) {
        this.currentname =  currentname;
    }

    public String getPdfpage() {
        return pdfpage;
    }
    public String getVsaccount() {
        return vsaccount;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    public String getDocpage() {
        return docpage;
    }

    public String getDoctotalpages() {
        return doctotalpages;
    }
    public String getAccounttype() {
        return accounttype;
    }
 @Override
   public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("PDFPage=").append(getPdfpage()).append("\n");
       sb.append("AccountType=").append(getAccounttype()).append("\n");
       sb.append("VSAccount=").append(getVsaccount()).append("\n");
       sb.append("Name=").append(getName()).append("\n");
       sb.append("Date=").append(getDate()).append("\n");
       sb.append("DocPage=").append(getDocpage()).append("\n");
       sb.append("DocTotalPages=").append(getDoctotalpages()).append("\n");
       return sb.toString();
   }
   
   public String toXML(){
       StringBuilder sb = new StringBuilder();
       sb.append("\t<BeginPage>").append(getPdfpage()).append("</BeginPage>\n");
       sb.append("\t<NumberOfPages>").append(getDoctotalpages()).append("</NumberOfPages>\n");
       sb.append("\t<DocumentKey>").append(getVsaccount()).append("</DocumentKey>\n");
       sb.append("\t<Indexes>\n");
       sb.append("\t<Index>\n");
       sb.append("\t<Name>AccountType</Name>\n");
       sb.append("\t<Value>").append(getAccounttype()).append("</Value>\n");
       sb.append("\t<Page>ALL</Page>\n");
       sb.append("\t</Index>\n");
       sb.append("\t<Index>\n");
       sb.append("\t<Name>VSAccount</Name>\n");
       sb.append("\t<Value>").append(getVsaccount()).append("</Value>\n");
       sb.append("\t<Page>ALL</Page>\n");
       sb.append("\t</Index>\n");
       sb.append("\t<Index>\n");
       sb.append("\t<Name>Name</Name>\n");
       sb.append("\t<Value>").append(getName().replaceAll("&", "&amp;")).append("</Value>\n");
       sb.append("\t<Page>ALL</Page>\n");
       sb.append("\t</Index>\n");
       sb.append("\t<Index>\n");
       sb.append("\t<Name>Date</Name>\n");
       sb.append("\t<Value>").append(getDate()).append("</Value>\n");
       sb.append("\t<Page>ALL</Page>\n");
       sb.append("\t</Index>\n");
       sb.append("\t</Indexes>\n");
       sb.append("\t<Overlays>\n\t<Overlay>\n\t<Name/>\n\t<Page/>\n\t</Overlay>\n\t</Overlays>\n\t<Inserts>\n\t<Insert>\n\t<Name/>\n\t</Insert>\n\t</Inserts>\n");
       
       return sb.toString();
   }
}
