-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, type, address, nif, vat) VALUES
('Tech Corp', 'private corp.', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', 'public administration', '456 Main Square', 'B87654321', 'VAT456');

-- Insert GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES
(1, 'John Doe', 'john@techcorp.com', '+34911234567'),
(2, 'Mary White', 'mary@citycouncil.gov', '+34912345678'),
(2, 'Robert Smith', 'robert@citycouncil.gov', '+34913456789');


-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES
(1, 1, 1, 5000.00, '2024-01-15', 'signed'),
(2, 2, 2, 2000.00, '2024-02-01', 'signed'),
(1, 1, 1, 2000.00, '2024-03-01', 'modified'),
(1, 1, 1, 3000.00, '2024-03-01', 'closed');

-- Insert Invoices
INSERT INTO Invoices (idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate, status) VALUES
(1, '2024-02-05', NULL, '2024-03-05', 5000.00, 10, 'draft'),
(2, '2024-01-20', '2024-01-22', '2024-02-20', 2000.00, 10, 'issued'),
(3, '2024-03-05', '2024-03-07', '2024-04-05', 3000.00, 10, 'paid');


-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES
(3, '2024-02-10', 3000.00);

-- Insert Activities
INSERT INTO Activities (name, status, dateStart, dateEnd, place) VALUES
('Olimpics 2024', 'planned', '2024-03-10', '2024-03-12', 'Convention Center'),
('ImpulsoTIC Week 2024', 'done', '2024-02-15', '2024-02-15', 'Central Park'),
('ImpulsoTIC Week 2023', 'closed', '2023-02-15', '2024-02-15', 'Central Park');

-- Insert Levels
INSERT INTO Levels (name, fee) VALUES
('Basic', 50.00);

-- Insert ActivityLevels
INSERT INTO ActivityLevels (idActivity, idLevel) VALUES
(1, 1),
(2, 1);

-- Insert Movements
INSERT INTO Movements (idActivity, type, concept, amount, date, receiptNumber, status) VALUES
(2, 'expense', 'Cleanup Supplies', -500.00, '2024-02-10', 'EXP-001', 'paid'),
(1, 'income', 'Ticket Sales', 3000.00, '2024-03-01', 'INV-002', 'paid'),
(1, 'expense', 'Venue Rental', -1500.00, '2024-03-05', 'EXP-002', 'paid'),
(1, 'income', 'Merchandise Sales', 800.00, '2024-03-10', 'INV-003', 'paid'),
(2, 'expense', 'Marketing Campaign', -1200.00, '2024-02-01', 'EXP-003', 'paid'),
(2, 'income', 'Donations', 1000.00, '2024-02-12', 'INV-004', 'paid'),
(2, 'expense', 'Event Catering', -700.00, '2024-02-15', 'EXP-004', 'paid'),
(1, 'expense', 'Security Services', -600.00, '2024-03-09', 'EXP-005', 'paid'),
(1, 'expense', 'Marketing Campaign', -2000.00, '2024-04-01', 'EXP-006', 'estimated'),
(2, 'income', 'Ticket Sales', 4000.00, '2024-04-10', 'INV-005', 'estimated');
