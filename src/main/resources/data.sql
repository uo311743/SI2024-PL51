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
(1, 'John Doe', 'john@techcorp.com', '555-1234'),
(2, 'Mary White', 'mary@citycouncil.gov', '555-5678');

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, activity, amount, date, status) VALUES
(1, 1, 'Tech Innovation Summit', 5000.00, '2024-01-15', 'signed'),
(2, 2, 'Community Cleanup', 2000.00, '2024-02-01', 'closed');

-- Insert Invoices
INSERT INTO Invoices (idSponsorshipAgreement, dateIssued, dateSent, dateExpiration, totalAmount, taxRate, status) VALUES
(1, '2024-01-20', '2024-01-22', '2024-02-20', 5500.00, 10, 'issued'),
(2, '2024-02-05', NULL, '2024-03-05', 2200.00, 10, 'draft');

-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES
(1, '2024-01-25', 5000.00),
(2, '2024-02-10', 2000.00);

-- Insert Activities
INSERT INTO Activities (name, status, dateStart, dateEnd, place) VALUES
('Olimpics 2024', 'planned', '2024-03-10', '2024-03-12', 'Convention Center'),
('ImpulsoTIC Week 2024', 'done', '2024-02-15', '2024-02-15', 'Central Park');

-- Insert Levels
INSERT INTO Levels (name, fee) VALUES
('Basic', 50.00);

-- Insert ActivityLevels
INSERT INTO ActivityLevels (idActivity, idLevel) VALUES
(1, 1),
(2, 1);

-- Insert Movements
INSERT INTO Movements (idActivity, type, concept, amount, date, receiptNumber, status) VALUES
(1, 'income', 'Sponsorship Payment', 5000.00, '2024-01-25', 'INV-001', 'paid'),
(2, 'expense', 'Cleanup Supplies', 500.00, '2024-02-10', 'EXP-001', 'paid');