-- Insert into SponsorOrganizations
INSERT INTO SponsorOrganizations (name, type, invoice_address, nif_vat) VALUES
('TechCorp', 'Private corp.', '123 Tech Street, Tech City', 'TC123456'),
('GreenGov', 'Public administration', '456 Government Ave, Capital', 'GG654321');

-- Insert sample Sponsor Organizations
INSERT INTO SponsorOrganizations (nameSponsorOrganization, typeSponsorOrganization, AddressSponsorOrganization, nifSponsorOrganization, vatSponsorOrganization)
VALUES 
  ('TechCorp', 'IT', '123 Main St', '12345678A', 'VAT12345678'),
  ('EduPartners', 'Education', '456 College Ave', '87654321B', 'VAT87654321'),
  ('GreenFuture', 'Environmental', '789 Green Blvd', '11223344C', 'VAT11223344');

-- Insert into GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');

-- Insert sample Sponsor Contacts
INSERT INTO SponsorContacts (idSponsorOrganization, nameSponsorContact, emailSponsorContact, phoneSponsorContact)
VALUES 
  (1, 'Alice Johnson', 'alice@techcorp.com', '555-0101'),
  (2, 'Bob Smith', 'bob@edupartners.com', '555-0202'),
  (3, 'Carol Lee', 'carol@greenfuture.com', '555-0303');

-- Insert into SponsorContact
INSERT INTO SponsorContact (sponsor_org_id, name, email, phone) VALUES
((SELECT rowid FROM SponsorOrganizations WHERE name='TechCorp'), 'Charlie Brown', 'charlie@techcorp.com', '123-456-7890'),
((SELECT rowid FROM SponsorOrganizations WHERE name='GreenGov'), 'Diana Prince', 'diana@greengov.gov', '098-765-4321');

-- Insert sample GB Members
INSERT INTO GBMembers (nameGBMember, roleGBMember)
VALUES 
  ('David Miller', 'Chair'),
  ('Emma Davis', 'Treasurer'),
  ('Frank Wilson', 'Secretary');

-- Insert sample Activities (names follow the rule: one of the 3 options + year)
INSERT INTO Activities (nameActivity, editionActivity, statusActivity, dateStartActivity, dateEndActivity, placeActivity)
VALUES 
  ('Olimpics 2023', '1st Edition', 'planned', '2023-07-01', '2023-07-07', 'Stadium A'),
  ('ImpulsoTIC Week 2023', '3rd Edition', 'opened', '2023-09-10', '2023-09-16', 'Convention Center'),
  ('Hour of Code 2023', '5th Edition', 'closed', '2023-11-01', '2023-11-01', 'Online');

-- Insert sample Movements for each activity
INSERT INTO Movements (idActivity, typeMovement, conceptMovement, amountMovement, dateMovement, ReceiptNumber, statusMovement)
VALUES 
  (1, 'income', 'Ticket Sales', 5000.00, '2023-06-15', 'RCPT1001', 'paid'),
  (2, 'expense', 'Venue Rental', 2000.00, '2023-09-05', 'RCPT2001', 'estimated'),
  (3, 'income', 'Sponsorship Fee', 3000.00, '2023-10-25', 'RCPT3001', 'paid');

-- Insert sample Levels
INSERT INTO Levels (nameLevel, feeLevel)
VALUES 
  ('Beginner', 50.00);

-- Insert sample ActivityLevel relationships
INSERT INTO ActivityLevel (idActivity, idLevel)
VALUES 
  (1, 1),  -- Olimpics 2023 with Beginner level
  (2, 1),  -- ImpulsoTIC Week 2023 with Beginner level
  (3, 1);  -- Hour of Code 2023 with Beginner level

-- Insert sample Sponsorship Agreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, amountSponsorshipAgreement, dateSponsorshipAgreement, statusSponsorshipAgreement)
VALUES 
  (1, 1, 10000.00, '2023-05-20', 'closed'),
  (2, 2, 8000.00, '2023-08-15', 'modified'),
  (3, 3, 12000.00, '2023-10-10', 'closed');
  
-- Insert sample Invoices
INSERT INTO Invoices (idSponsorshipAgreement, dateIssueInvoice, dateSentInvoice, dateExpirationInvoice, totalAmountInvoice, taxRateInvoice, statusInvoice)
VALUES 
  (1, '2023-05-25', '2023-05-26', '2023-06-25', 10000.00, 21.0, 'issued'),
  (2, '2023-08-20', '2023-08-21', '2023-09-20', 8000.00, 21.0, 'issued'),
  (3, '2023-10-15', '2023-10-16', '2023-11-15', 12000.00, 21.0, 'issued');

-- Insert sample Sponsorship Payments
INSERT INTO SponsorshipPayments (idInvoice, dateSponsorshipPayment, amountSponsorshipPayment)
VALUES 
  (1, '2023-06-01', 10000.00),
  (2, '2023-09-01', 4000.00),
  (2, '2023-09-15', 4000.00),
  (3, '2023-11-01', 12000.00);