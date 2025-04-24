-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, address, nif, vat) VALUES
('Tech Corp', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', '456 Main Square', 'B87654321', 'VAT456');

-- Insert GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES
(1, 'John Doe', 'uo294319@uniovi.es', '+34911234567'),
(2, 'Mary White', 'uo294319@uniovi.es', '+34912345678'),
(2, 'Robert Smith', 'uo294319@uniovi.es', '+34913456789');

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, status) VALUES
(1, 1, 1, 5000.00, '2024-01-15', 'signed'),
(2, 2, 2, 2000.00, '2024-02-01', 'signed'),
(1, 1, 1, 2000.00, '2024-03-01', 'modified'),
(1, 1, 1, 3000.00, '2024-03-01', 'closed');

-- Insert Invoices
INSERT INTO Invoices (id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) VALUES
('INV-001', 1, '2024-02-05', 5000.00, 10, 'issued'),
('INV-002', 2, '2024-01-20', 2000.00, 10, 'issued'),
('INV-003', 3, '2024-03-05', 3000.00, 10, 'paid');

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
(2, 'Basic', 100.00),
(3, 'Basic', 120.00);

-- Insert sample data into IncomesExpenses (ensuring all have at least one movement)
INSERT INTO IncomesExpenses (idActivity, type, status, amountEstimated, dateEstimated, concept) 
VALUES 
(1, 'income', 'estimated', 5000.00, '2024-03-01', 'Sponsorship from Company A'),
(1, 'income', 'estimated', 2000.00, '2024-03-05', 'Ticket Sales'),
(2, 'income', 'estimated', 3500.00, NULL, 'Donations'),
(1, 'expense', 'estimated', -1500.00, '2024-03-02', 'Venue Rental'),
(1, 'expense', 'estimated', -800.00, '2024-03-06', 'Catering'),
(2, 'expense', 'estimated', -1200.00, '2024-03-12', 'Equipment Rental');

-- Insert sample data into Movements (ensuring all incomes/expenses have at least one movement)
INSERT INTO Movements (idType, concept, amount, date) 
VALUES 
(1, 'First payment from Company A', 2500.00, '2024-03-01'),
(1, 'Final payment from Company A', 2500.00, '2024-03-02'),
(2, 'Online ticket sales', 1200.00, '2024-03-05'),
(2, 'On-site ticket sales', 800.00, '2024-03-06'),
(3, 'Donation from John Doe', 2000.00, '2024-03-10'),
(3, 'Donation from Jane Smith', 1500.00, '2024-03-11'),
(4, 'Venue deposit', -750.00, '2024-03-02'),
(4, 'Final venue payment', -750.00, '2024-03-03'),
(5, 'Catering initial payment', -400.00, '2024-03-06'),
(5, 'Catering final payment', -400.00, '2024-03-07'),
(6, 'Equipment rental fee', -1200.00, '2024-03-12');

-- Insert ActivityTemplates
INSERT INTO ActivityTemplates (name) VALUES
('Informatics Olympics'),
('ImpulsoTIC Week'),
('Hour of code');
