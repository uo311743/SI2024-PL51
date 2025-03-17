-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, type, address, nif, vat) VALUES
('Tech Corp', 'private', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', 'public', '456 Main Square', 'B87654321', 'VAT456');

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
INSERT INTO Invoices (id, idSponsorshipAgreement, dateIssued, dateExpiration, totalAmount, taxRate, status) VALUES
('INV-001', 1, '2024-02-05', '2024-04-05', 5000.00, 10, 'issued'),
('INV-002', 2, '2024-01-20', '2024-03-20', 2000.00, 10, 'issued'),
('INV-003', 3, '2024-03-05', '2024-05-04', 3000.00, 10, 'paid');

-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES
('INV-003', '2024-02-10', 3000.00);

-- Insert Activities
INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES
('Informatics Olimpics 2024', 13, 'planned', '2024-03-10', '2024-03-12', 'Convention Center'),
('ImpulsoTIC Week 2024', 12, 'planned', '2024-02-15', '2024-02-15', 'Central Park'),
('ImpulsoTIC Week 2023', 11, 'closed', '2023-02-15', '2024-02-15', 'Central Park');

-- Insert Levels
INSERT INTO Levels (idActivity, name, fee) VALUES
(1, 'Basic', 50.00),
(2, 'Basic', 100.00);

-- Insert Movements
INSERT INTO Movements (idActivity, type, concept, amount, date, status) VALUES
(2, 'expense', 'Cleanup Supplies', 500.00, '2024-02-10', 'paid'),
(1, 'income', 'Ticket Sales', 3000.00, '2024-03-01', 'paid'),
(1, 'expense', 'Venue Rental', 1500.00, '2024-03-05', 'paid'),
(1, 'income', 'Merchandise Sales', 800.00, '2024-03-10', 'paid'),
(2, 'expense', 'Marketing Campaign', 1200.00, '2024-02-01', 'paid'),
(2, 'income', 'Donations', 1000.00, '2024-02-12', 'paid'),
(2, 'expense', 'Event Catering', 700.00, '2024-02-15', 'paid'),
(1, 'expense', 'Security Services', 600.00, '2024-03-09', 'paid'),
(1, 'expense', 'Marketing Campaign', 2000.00, '2024-04-01', 'estimated'),
(2, 'income', 'Ticket Sales', 4000.00, '2024-04-10', 'estimated');

-- Insert ActivityTemplates
INSERT INTO ActivityTemplates (name) VALUES
('Informatics Olympics'),
('ImpulsoTIC Week'),
('Hour of code');
