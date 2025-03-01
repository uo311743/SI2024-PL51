-- Insert into SponsorOrganizations
INSERT INTO SponsorOrganizations (name, type, invoice_address, nif_vat) VALUES
('TechCorp', 'Private corp.', '123 Tech Street, Tech City', 'TC123456'),
('GreenGov', 'Public administration', '456 Government Ave, Capital', 'GG654321');

-- Insert into GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');

-- Insert into SponsorContact
INSERT INTO SponsorContact (sponsor_org_id, name, email, phone) VALUES
((SELECT rowid FROM SponsorOrganizations WHERE name='TechCorp'), 'Charlie Brown', 'charlie@techcorp.com', '123-456-7890'),
((SELECT rowid FROM SponsorOrganizations WHERE name='GreenGov'), 'Diana Prince', 'diana@greengov.gov', '098-765-4321');

-- Insert into Activities
INSERT INTO Activities (name, edition, state, date_celebration, place_celebration, estimated_income, estimated_expenses) VALUES
('Tech Conference', 2025, 'planning', '2025-06-15', 'Convention Center', 50000, 30000),
('Green Summit', 2025, 'opened', '2025-04-10', 'Eco Park', 70000, 40000);

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

-- Insert into ActivityLevel
INSERT INTO ActivityLevel (activity_id, level_id) VALUES
((SELECT rowid FROM Activities WHERE name='Tech Conference' AND edition=2025), (SELECT id FROM Levels WHERE name='Gold')),
((SELECT rowid FROM Activities WHERE name='Green Summit' AND edition=2025), (SELECT id FROM Levels WHERE name='Silver'));

-- Insert into IncomesExpenses
INSERT INTO IncomesExpenses (concept, amount, date_movement, status) VALUES
('Sponsorship Payment - TechCorp', 20000, '2025-01-30', 'paid'),
('Sponsorship Payment - GreenGov', 30000, '2025-02-28', 'paid'),
('Venue Rental - Tech Conference', -15000, '2025-06-01', 'unpaid'),
('Catering - Green Summit', -8000, '2025-04-05', 'paid');