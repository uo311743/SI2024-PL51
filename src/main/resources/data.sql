-- Insert SponsorOrganizations
INSERT INTO SponsorOrganizations (name, address, nif, vat) VALUES
('Tech Corp', '123 Tech Street', 'A12345678', 'VAT123'),
('City Council', '456 Main Square', 'B87654321', 'VAT456'),
('Caja Rural de Asturias', '123 Tech Street', 'C92345678', 'VAT999'),
('Leyenda SL', '123 Tech Street', 'D82345678', 'VAT899');

-- Insert GBMembers
INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer');

-- Insert SponsorContacts
INSERT INTO SponsorContacts (idSponsorOrganization, name, email, phone) VALUES
(1, 'John Doe', 'uo294319@uniovi.es', '+34911234567'),
(2, 'Mary White', 'uo294319@uniovi.es', '+34912345678'),
(4, 'Robert Smith', 'uo294319@uniovi.es', '+34913456789'),
(3, 'Charles Smith', 'uo294319@uniovi.es', '+34913456789');

-- Insert SponsorshipAgreements
INSERT INTO SponsorshipAgreements (idSponsorContact, idGBMember, idActivity, amount, date, endDate, status) VALUES
(1, 1, 1, 5000.00, '2024-01-15', NULL, 'signed'),
(2, 2, 2, 2000.00, '2024-02-01', NULL, 'signed'),
(1, 1, 1, 2000.00, '2024-03-01', NULL, 'modified'),
(1, 1, 1, 3000.00, '2024-03-01', NULL, 'closed'),
(4, 1, 1, 750, '2025-10-01', NULL, 'signed'),
(3, 1, 1, 14000, '2025-01-01', '2026-12-31', 'signed');

/*INSERT INTO LongTermAgreementActivities (idSponsorshipAgreement, idActivity, amount) VALUES
(6, 5, 7000),
(6, 1, 7000);*/

-- Insert Invoices
INSERT INTO Invoices (id, idSponsorshipAgreement, dateIssued, totalAmount, taxRate, status) VALUES
('INV-001', 1, '2024-02-05', 5000.00, 21, 'issued'),
('INV-002', 2, '2024-01-20', 2000.00, 21, 'issued'),
('INV-003', 4, '2024-03-05', 3000.00, 21, 'paid');

-- Insert SponsorshipPayments
INSERT INTO SponsorshipPayments (idInvoice, date, amount) VALUES
('INV-003', '2024-02-10', 3000.00*1.21);

-- Insert Activities
INSERT INTO Activities (name, edition, status, dateStart, dateEnd, place) VALUES
('Hour of Code 2025', 13, 'planned', '2025-12-08', '2025-12-15', 'Convention Center'),
('ImpulsoTIC Week 2024', 12, 'closed', '2024-11-15', '2024-11-15', 'Central Park'),
('ImpulsoTIC Week 2025', 11, 'planned', '2025-11-10', '2025-11-14', 'Central Park'),
('Hour of Code 2026', 13, 'planned', '2026-12-08', '2026-12-15', 'Convention Center');

-- Insert Levels
INSERT INTO Levels (idActivity, name, fee) VALUES
(3, 'Gold', 4000.00),
(3, 'Silver', 2000.00),
(1, 'Basic', 120.00);

-- Insert sample data into IncomesExpenses (ensuring all have at least one movement)
INSERT INTO IncomesExpenses (idActivity, type, status, amountEstimated, dateEstimated, concept) 
VALUES 
(1, 'income', 'estimated', 5000.00, '2024-03-01', 'Sponsorship from Company A'),
(1, 'income', 'estimated', 2000.00, '2024-03-05', 'Ticket Sales'),
(2, 'income', 'paid', 1000.00, '2024-10-02', 'CCII Grant'),
(2, 'expense', 'paid', -6000.00, '2024-10-02', 'Communication services'),
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
