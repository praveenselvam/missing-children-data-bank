## Script to pull data from Google Spreadsheet and import to our database.
import sys
import os
import socket
import logging
import gdata.service
import gdata.spreadsheet
import gdata.spreadsheet.service
import gdata.spreadsheet.text_db

def stringify(raw_string):
    if raw_string is None:
        return ""
    return raw_string
                                                       
class MasterdataImporter():
    def run(self):
        self.mapping = {}
        self.mapping['regno'] = 'homeAdmissionId'
        self.mapping['name'] = 'name'
        self.mapping['fathersname'] = 'parent'
        self.mapping['age'] = 'age'
        self.mapping['cwcno'] = 'cwcId'
        self.mapping['stateplace'] = 'state'
        self.getGoogleEntries()

    def child_xml(self, record):
        child_xml_start = "<child>"
        child_xml_end = "</child>"
        child_xml_body = ""
        
        for key in record:
            if key in self.mapping:
                child_xml_body += " <"+str(self.mapping[key])+">"+stringify(record[key])+"</"+str(self.mapping[key])+">"
    
        if child_xml_body != "":
            return child_xml_start + child_xml_body + child_xml_end
        return None
    
    def build_import_xml(self, records):
        if len(records) == 0:
            return None
        xml_hdr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        xml_start = "<import>\n"
        xml_end = "</import>"
        xml_body = ""
        for record in records:
            xml_body += record +"\n"
        return xml_hdr + xml_start + xml_body + xml_end

    def getGoogleEntries(self):                
        gd_client = gdata.spreadsheet.service.SpreadsheetsService()
        gd_client.email = os.environ['EMAIL']
        gd_client.password = os.environ['PASSWORD']

        try:                    
	    	# log in
            gd_client.ProgrammaticLogin()
        except socket.sslerror, e:
            logging.error('Spreadsheet socket.sslerror: ' + str(e))
            return False
	
        q = gdata.spreadsheet.service.DocumentQuery()
        q['title']='GBH Master'
        q['title-exact']='true'
        feed = gd_client.GetSpreadsheetsFeed(query=q)
        spreadsheet_id = feed.entry[0].id.text.rsplit('/',1)[1]
        feed = gd_client.GetWorksheetsFeed(spreadsheet_id)
        worksheet_id = feed.entry[0].id.text.rsplit('/',1)[1]
        q = gdata.spreadsheet.service.ListQuery()
        q.orderby = 'column:regno'

        try:
	        # fetch the spreadsheet data
            feed = gd_client.GetListFeed(spreadsheet_id, worksheet_id, query=q)
        except gdata.service.RequestError, e:
            logging.error('Spreadsheet gdata.service.RequestError: ' + str(e))
            return False
        except socket.sslerror, e:
            logging.error('Spreadsheet socket.sslerror: ' + str(e))
            return False
        
        records=[]
        for row_entry in feed.entry:
	        # to get the column data out, you use the text_db.Record class, then use the dict record.content
            record = gdata.spreadsheet.text_db.Record(row_entry=row_entry)
            fields={}
            for key in record.content:
                fields[key]=record.content[key]

            child_xml_snippet = self.child_xml(fields)

            if child_xml_snippet is not None:
                records.append(child_xml_snippet)

        import_xml = self.build_import_xml(records)
        print import_xml

if __name__ == "__main__":
    MasterdataImporter().run()
