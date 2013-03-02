## Script to pull data from Google Spreadsheet and import to our database.
import sys
import os
import socket
import logging
import gdata.service
import gdata.spreadsheet
import gdata.spreadsheet.service
import gdata.spreadsheet.text_db
                                                       
class MasterdataImporter():
    def run(self):
        self.getGoogleEntries()

    def getGoogleEntries(self):                
        gd_client = gdata.spreadsheet.service.SpreadsheetsService()
        gd_client.email = os.environ['EMAIL']
        gd_client.password = os.environ['PASSWORD']

        try:                    
	    	# log in
            gd_client.ProgrammaticLogin()
            print "Login accepted!"
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
        print "Spreadsheet id %s, Worksheet id %s" %(spreadsheet_id, worksheet_id)
        q = gdata.spreadsheet.service.ListQuery()
        q.orderby = 'column:admn'

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
        # Iterate over the rows
        for row_entry in feed.entry:
	        # to get the column data out, you use the text_db.Record class, then use the dict record.content
            record = gdata.spreadsheet.text_db.Record(row_entry=row_entry)
            fields=[]
            for key in record.content:
                fields.append((key, record.content[key]))
            
            records.append(fields)

        ## TODO:
        # Map the fields on the spreadsheet to the child model fields
        # print records

if __name__ == "__main__":
    MasterdataImporter().run()