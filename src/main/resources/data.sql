<<<<<<< HEAD
-- Insert into SponsorOrganizations
INSERT INTO SponsorOrganizations (name, type, invoice_address, nif_vat) VALUES
('TechCorp', 'Private corp.', '123 Tech Street, Tech City', 'TC123456'),
('GreenGov', 'Public administration', '456 Government Ave, Capital', 'GG654321');
=======
-- Insert sample Sponsor Organizations
INSERT INTO SponsorOrganizations (nameSponsorOrganization, typeSponsorOrganization, AddressSponsorOrganization, nifSponsorOrganization, vatSponsorOrganization)
VALUES 
  ('TechCorp', 'IT', '123 Main St', '12345678A', 'VAT12345678'),
  ('EduPartners', 'Education', '456 College Ave', '87654321B', 'VAT87654321'),
  ('GreenFuture', 'Environmental', '789 Green Blvd', '11223344C', 'VAT11223344');
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');
=======
-- Insert sample Sponsor Contacts
INSERT INTO SponsorContacts (idSponsorOrganization, nameSponsorContact, emailSponsorContact, phoneSponsorContact)
VALUES 
  (1, 'Alice Johnson', 'alice@techcorp.com', '555-0101'),
  (2, 'Bob Smith', 'bob@edupartners.com', '555-0202'),
  (3, 'Carol Lee', 'carol@greenfuture.com', '555-0303');
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into SponsorContact
INSERT INTO SponsorContact (sponsor_org_id, name, email, phone) VALUES
((SELECT rowid FROM SponsorOrganizations WHERE name='TechCorp'), 'Charlie Brown', 'charlie@techcorp.com', '123-456-7890'),
((SELECT rowid FROM SponsorOrganizations WHERE name='GreenGov'), 'Diana Prince', 'diana@greengov.gov', '098-765-4321');
=======
-- Insert sample GB Members
INSERT INTO GBMembers (nameGBMember, roleGBMember)
VALUES 
  ('David Miller', 'Chair'),
  ('Emma Davis', 'Treasurer'),
  ('Frank Wilson', 'Secretary');
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into Activities
INSERT INTO Activities (name, edition, state, date_celebration, place_celebration, estimated_income, estimated_expenses) VALUES
('Tech Conference', 2025, 'planning', '2025-06-15', 'Convention Center', 50000, 30000),
('Green Summit', 2025, 'opened', '2025-04-10', 'Eco Park', 70000, 40000);
=======
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
  ('Beginner', 50.00),
  ('Intermediate', 75.00),
  ('Advanced', 100.00);

-- Insert sample ActivityLevel relationships
INSERT INTO ActivityLevel (idActivity, idLevel)
VALUES 
  (1, 1),  -- Olimpics 2023 with Beginner level
  (2, 2),  -- ImpulsoTIC Week 2023 with Intermediate level
  (3, 3);  -- Hour of Code 2023 with Advanced level
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into SponsorshipAgreements
INSERT INTO SponsorshipAgreements (sponsor_org_id, activity, amount, date_agreement) VALUES
('TC123456', 'Tech Conference', 20000, '2025-01-10'),
('GG654321', 'Green Summit', 30000, '2025-02-15');

-- Insert into Invoices
INSERT INTO Invoices (sponsorship_agreement_id, date_issue, date_send) VALUES
(1, '2025-01-20', '2025-01-22'),
(2, '2025-02-20', '2025-02-22');

-- Insert into SponsorshipPayments
INSERT INTO SponsorshipPayments (invoice_id, date_received, amount) VALUES
(1, '2025-01-30', 20000),
(2, '2025-02-28', 30000);

-- Insert into Levels
INSERT INTO Levels (name, fee) VALUES
('Gold', 10000),
('Silver', 5000);
=======
-- Insert sample Sponsorship Agreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMembers, amountSponsorshipAgreement, dateSponsorshipAgreement, statusSponsorshipAgreement)
VALUES 
  (1, 1, 10000.00, '2023-05-20', 'closed'),
  (2, 2, 8000.00, '2023-08-15', 'modified'),
  (3, 3, 12000.00, '2023-10-10', 'closed');
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into ActivityLevel
INSERT INTO ActivityLevel (activity_id, level_id) VALUES
((SELECT rowid FROM Activities WHERE name='Tech Conference' AND edition=2025), (SELECT id FROM Levels WHERE name='Gold')),
((SELECT rowid FROM Activities WHERE name='Green Summit' AND edition=2025), (SELECT id FROM Levels WHERE name='Silver'));
=======
-- Insert sample Invoices
INSERT INTO Invoices (idSponsorshipAgreement, dateIssueInvoice, dateSentInvoice, dateExpirationInvoice, totalAmountInvoice, taxRateInvoice, statusInvoice)
VALUES 
  (1, '2023-05-25', '2023-05-26', '2023-06-25', 10000.00, 21.0, 'issued'),
  (2, '2023-08-20', '2023-08-21', '2023-09-20', 8000.00, 21.0, 'issued'),
  (3, '2023-10-15', '2023-10-16', '2023-11-15', 12000.00, 21.0, 'issued');
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git

<<<<<<< HEAD
-- Insert into IncomesExpenses
INSERT INTO IncomesExpenses (concept, amount, date_movement, status) VALUES
('Sponsorship Payment - TechCorp', 20000, '2025-01-30', 'paid'),
('Sponsorship Payment - GreenGov', 30000, '2025-02-28', 'paid'),
('Venue Rental - Tech Conference', -15000, '2025-06-01', 'unpaid'),
('Catering - Green Summit', -8000, '2025-04-05', 'paid');
=======
-- Insert sample Sponsorship Payments
INSERT INTO SponsorshipPayments (idInvoice, dateSponsorshipPayment, amountSponsorshipPayments)
VALUES 
  (1, '2023-06-01', 10000.00),
  (2, '2023-09-01', 4000.00),
  (2, '2023-09-15', 4000.00),
  (3, '2023-11-01', 12000.00);
>>>>>>> branch 'develop' of https://github.com/uo311743/coiipa.git
